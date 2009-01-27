/*  This software code is made available "AS IS" without warranties of any
	kind.  You may copy, display, modify and redistribute the software
	code either by itself or as incorporated into your code; provided that
	you do not remove any proprietary notices.  Your use of this software
	code is at your own risk and you waive any claim against Amazon
	Web Services LLC or its affiliates with respect to your use of this
	software code. (c) Amazon Web Services LLC or its affiliates.*/

/*This class reads the caller credentials specified in the config.java file.
It then makes InstallPaymentInstruction API calls to install the user specific token on the Caller's account.*/

package com.easyinsight.billing;

import com.easyinsight.logging.LogClass;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.util.*;

public class InstallPaymentInstruction{

	//this method builds the httpQuery

    public String payRequest(String recipientTokenID, String callerTokenID, String senderTokenID, String transactionAmount, String callerReference) throws UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("RecipientTokenID=" + URLEncoder.encode(recipientTokenID, "UTF-8"));
        urlBuilder.append("CallerTokenID=" + URLEncoder.encode(callerTokenID, "UTF-8"));
        urlBuilder.append("SenderTokenID=" + URLEncoder.encode(senderTokenID, "UTF-8"));
        urlBuilder.append("TransactionAmount=" + URLEncoder.encode(transactionAmount, "UTF-8"));
        urlBuilder.append("CallerReference=" + URLEncoder.encode(callerReference, "UTF-8"));
        urlBuilder.append("ChargeFeeTo=" + URLEncoder.encode("Recipient", "UTF-8"));
        return urlBuilder.toString();
    }

    public String getHttpQuery(String apiName,String CallerReference,String PaymentInstruction,String TokenFriendlyName,String TokenType,String PaymentReason) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		String queryString = "";
		StringBuffer stringToSign = new StringBuffer("");

		Map<String, String> propertyMap = new HashMap<String, String>();
		propertyMap.put("AWSAccessKeyId", Config.AWSAccessKeyId);
		propertyMap.put("CallerReference",CallerReference );
		propertyMap.put("PaymentInstruction",PaymentInstruction );
		propertyMap.put("TokenFriendlyName",TokenFriendlyName );
		propertyMap.put("TokenType",TokenType );
		propertyMap.put("PaymentReason",PaymentReason );
		propertyMap.put("Action", apiName);
		propertyMap.put("Version", Config.Version);
		propertyMap.put("SignatureVersion", Config.SignatureVersion);
		propertyMap.put("Timestamp", FormatTimestamp.formatTimestamp());

		List<String> allPropertyNames = new ArrayList<String>(propertyMap.keySet());
		Collections.sort(allPropertyNames, String.CASE_INSENSITIVE_ORDER);

		for (String propertyName : allPropertyNames) {
			stringToSign.append(propertyName).append(propertyMap.get(propertyName));
		}

		// signing the request
		String signature = GetSignature.calculateRFC2104HMAC(new String(stringToSign),
				Config.SecretKey);

		// once we have the signature form the query string and append the signature to it
		for (String property : allPropertyNames) {
			queryString += property+"="+URLEncoder.encode(propertyMap.get(property), "UTF-8") + "&";
		}

		return (queryString + "Signature=" + URLEncoder.encode(signature, "UTF-8"));
	}

    public String createSenderQuery(String transactionAmount, String callerTokenID, String recipientTokenID, String pipelineName,
                                     String callerReference) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        // transaction amount
        // caller reference
        // pipeline name
        // recipient token
        // caller key
        // return URL = my URL
        String returnURL = "http://localhost:8080/DMS/easyui-debug/PurchaseMade.html?callerReference=" + callerReference;
        String queryString = "";
		StringBuffer stringToSign = new StringBuffer("");

		Map<String, String> propertyMap = new HashMap<String, String>();
		propertyMap.put("transactionAmount", transactionAmount);
        LogClass.debug("*** caller reference = " + callerReference);
        propertyMap.put("callerReference", callerReference );
		propertyMap.put("pipelineName", pipelineName);
        LogClass.debug("*** recipient token = " + recipientTokenID);
        //propertyMap.put("recipientToken", recipientTokenID);
		propertyMap.put("callerKey", Config.AWSAccessKeyId);
        propertyMap.put("returnURL", returnURL);

        List<String> allPropertyNames = new ArrayList<String>(propertyMap.keySet());
		Collections.sort(allPropertyNames, String.CASE_INSENSITIVE_ORDER);

        stringToSign.append("/cobranded-ui/actions/start?");
        Iterator<String> iter = allPropertyNames.iterator();
        while (iter.hasNext()) {
            String propertyName = iter.next();
            stringToSign.append(propertyName);
            stringToSign.append("=");
            stringToSign.append(URLEncoder.encode(propertyMap.get(propertyName), "UTF-8"));
            if (iter.hasNext()) {
                stringToSign.append("&");
            }
        }
        /*for (String propertyName : allPropertyNames) {
			stringToSign.append(propertyName).append(propertyMap.get(propertyName));
		}*/

		// signing the request
		String signature = GetSignature.calculateRFC2104HMAC(new String(stringToSign),
				Config.SecretKey);

		// once we have the signature form the query string and append the signature to it
		for (String property : allPropertyNames) {
			queryString += property+"="+URLEncoder.encode(propertyMap.get(property), "UTF-8") + "&";
		}

		return "https://authorize.payments-sandbox.amazon.com/cobranded-ui/actions/start?" + (queryString + "awsSignature=" + URLEncoder.encode(signature, "UTF-8"));
    }

    public static void main(String[] args) throws Exception {

        InstallPaymentInstruction obj = new InstallPaymentInstruction();
        String callerID;
        {
            String callerReference = "Caller" + System.currentTimeMillis() + (Math.random() * 100000);
            String apiName = "InstallPaymentInstruction";
            String PaymentInstruction = "MyRole == 'Caller';";
            String TokenFriendlyName = "AmazonFPSSDK - Caller";
            String TokenType = "Unrestricted";
            String PaymentReason = "paymentReason";
            String httpQuery = obj.getHttpQuery(apiName, callerReference, PaymentInstruction, TokenFriendlyName, TokenType, PaymentReason);
            Driver installPI = new Driver(httpQuery);
            callerID = installPI.call();
        }

        String recipientID;
        {
            String callerReference = "Caller" + System.currentTimeMillis() + (Math.random() * 100000);
            String apiName = "InstallPaymentInstruction";
            //String CallerReference = "AmazonFPSSDK - Recipient000";
            String PaymentInstruction = "MyRole == 'Recipient';";
            String TokenFriendlyName = "AmazonFPSSDK - Recipient";
            String TokenType = "Unrestricted";
            String PaymentReason = "paymentReason";
            String httpQuery = obj.getHttpQuery(apiName, callerReference, PaymentInstruction, TokenFriendlyName, TokenType, PaymentReason);
            Driver installPI = new Driver(httpQuery);
            recipientID = installPI.call();
        }

        String callerReference = "EasyInsight" + System.currentTimeMillis();
        String queryURL = obj.createSenderQuery("10", callerID, recipientID, "SingleUse", callerReference);
        LogClass.debug(queryURL);
    }

}