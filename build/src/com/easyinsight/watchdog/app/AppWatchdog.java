package com.easyinsight.watchdog.app;

import org.jets3t.service.S3ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.security.AWSCredentials;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;

import java.io.*;
import java.util.Date;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;
import java.net.URL;
import java.net.URLConnection;

/**
 * User: James Boe
 * Date: Jan 27, 2009
 * Time: 12:55:27 AM
 */
public class AppWatchdog {

    public static final String[] SHUTDOWN_CMD = {"/opt/tomcat/bin/catalina.sh", "stop"};
    public static final String[] STARTUP_CMD = {"/opt/tomcat/bin/catalina.sh", "start"};
    public static final String[] DUMP = {"pkill", "-f", "-3", "catalina"};
    public static final String[] KILL = {"pkill", "-f", "-9", "catalina"};
    public static final String[] GREP = {"pgrep", "-f", "catalina"};

    public void kill() {
        try {
            Runtime.getRuntime().exec(DUMP);
            Thread.sleep(2000);
            Runtime.getRuntime().exec(KILL);

            Date d = new Date();
            boolean found = false;
            do {
                Process p = Runtime.getRuntime().exec(GREP);
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

                found = false;
                String line;
                while ((line = br.readLine()) != null) {
                    try {
                        if (Long.parseLong(line) != 0)
                            found = true;
                    } catch (NumberFormatException e) {
                    }
                }
            } while(found && ((new Date()).getTime() - d.getTime()) < 60);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void restart() {
        try {
            StreamLogger.logProcess(Runtime.getRuntime().exec(SHUTDOWN_CMD), "shutdown");
            boolean shutdownSuccessful = false;
            int retryCount = 0;
            while (!shutdownSuccessful && retryCount < 10) {
                try {
                    Thread.sleep(5000);
                    URL url = new URL("http://localhost:8080");
                    URLConnection connection = url.openConnection();
                    InputStream content = connection.getInputStream();
                    content.read();
                    System.out.println("successfully opened connection...");
                    retryCount++;
                } catch (Exception e) {
                    shutdownSuccessful = true;
                }
            }
            StreamLogger.logProcess(Runtime.getRuntime().exec(STARTUP_CMD), "startup");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        try {
            System.out.println("Running shutdown...");
            StreamLogger.logProcess(Runtime.getRuntime().exec(SHUTDOWN_CMD), "shutdown");
            // need to verify...
            boolean shutdownSuccessful = false;
            int retryCount = 0;
            while (!shutdownSuccessful && retryCount < 10) {
                try {
                    Thread.sleep(5000);
                    URL url = new URL("http://localhost:8080");
                    URLConnection connection = url.openConnection();
                    InputStream content = connection.getInputStream();
                    content.read();
                    System.out.println("successfully opened connection...");
                    retryCount++;
                } catch (Exception e) {
                    shutdownSuccessful = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            System.out.println("Running Startup....");
            StreamLogger.logProcess(Runtime.getRuntime().exec(STARTUP_CMD), "startup");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void download(String role, String type) {
        FileOutputStream fos = null, setEnv = null, eiconfig = null;
        try {
            File f = new File("/opt/tomcat/code.zip");
            fos = replaceFile(f);
            AWSCredentials credentials = new AWSCredentials("0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
            RestS3Service s3Service = new RestS3Service(credentials);
            S3Bucket bucket = s3Service.getBucket("eiproduction");
            S3Bucket stagingBucket = s3Service.getBucket("eistaging");
            S3Bucket codeBucket = role.toLowerCase().contains("staging") ? stagingBucket : bucket;
            S3Object object = s3Service.getObject(codeBucket, "code.zip");
            writeFile(object, fos);
            S3Object setEnvObject = s3Service.getObject(bucket, "setenv.sh-" + role + "-" + type);
            File m = new File("/opt/watchdog/setenv.sh");
            setEnv = replaceFile(m);
            writeFile(setEnvObject, setEnv);
            File props = new File("/opt/watchdog/eiconfig.properties");
            S3Object propertiesDownload = s3Service.getObject(bucket, "eiconfig.properties-" + role + "-" + type);
            eiconfig = replaceFile(props);
            writeFile(propertiesDownload, eiconfig);
            eiconfig.flush();
            fos.flush();
            setEnv.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(setEnv != null) {
                try {
                    setEnv.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(eiconfig != null) {
                try {
                    eiconfig.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeFile(S3Object inputObject, FileOutputStream file) throws S3ServiceException, IOException {
        InputStream is = inputObject.getDataInputStream();
        BufferedOutputStream bufOS = new BufferedOutputStream(file, 8192);
        int nBytes;
        byte[] buffer = new byte[8192];
        while ((nBytes = is.read(buffer)) != -1) {
            bufOS.write(buffer, 0, nBytes);
            bufOS.flush();
        }
    }

    public static FileOutputStream replaceFile(File file) throws IOException {
        if (file.exists()) {
            boolean success = file.delete();
            if (!success) {
                throw new RuntimeException("Could not delete existing " + file.getName());
            }
        }
        boolean success = file.createNewFile();
        if (!success) {
            throw new RuntimeException("Could not create new " + file.getName());
        }
        FileOutputStream fos = new FileOutputStream(file);
        return fos;
    }

    public void update() {
        try {
            File file = new File("/opt/tomcat/code.zip");
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis, 8192);
            ZipInputStream zin = new ZipInputStream(bis);
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                System.out.println("Unzipping " + ze.getName());
                if (ze.isDirectory()) {
                    File directory = new File("/opt/tomcat/webapps/app/" + ze.getName());
                    if (!directory.exists()) {
                        if (!directory.mkdir()) {
                            throw new RuntimeException("couldn't create directory " + directory.getAbsolutePath());
                        }
                    }
                } else {
                    byte[] buffer = new byte[8192];
                    FileOutputStream fout = new FileOutputStream("/opt/tomcat/webapps/app/" + ze.getName());
                    BufferedOutputStream bufOS = new BufferedOutputStream(fout, 8192);
                    int nBytes;
                    while ((nBytes = zin.read(buffer)) != -1) {
                        bufOS.write(buffer, 0, nBytes);
                    }

                    bufOS.close();
                    fout.close();
                }
                zin.closeEntry();
            }
            zin.close();
            copyFile("/opt/watchdog/eiconfig.properties", "/opt/tomcat/webapps/app/WEB-INF/classes/eiconfig.properties");
            copyFile("/opt/watchdog/setenv.sh", "/opt/tomcat/bin/setenv.sh").setExecutable(true, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File copyFile(String fileName, String targetName) throws IOException {
        File file = new File(fileName);
        File target = new File(targetName);
        target.delete();
        FileReader reader = new FileReader(file);
        FileWriter writer = new FileWriter(target);
        int byteSymbol;
        while ((byteSymbol = reader.read()) != -1) {
            writer.write(byteSymbol);
        }
        writer.close();
        reader.close();
        return target;
    }

    public static void main(String[] args) throws IOException {
            new AppWatchdog().download("Staging", "m1.small");
    }
}
