package com.easyinsight.billing;

import com.easyinsight.amazon.*;
import com.easyinsight.logging.LogClass;

import javax.xml.rpc.ServiceException;
import java.net.URL;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.math.BigInteger;

/**
 * User: James Boe
 * Date: Aug 3, 2008
 * Time: 11:55:04 AM
 */
public class Pay {

    public static AmazonFPSPortType setup() throws MalformedURLException,
            ServiceException {
        /*---------------------------- Begin SETUP --------------------------------*/
        /*
           * Set System configuration. This step constructs the SOAP Headers with the configuration provided in the
           * client-config.wsdd. This also gives the location and password for the SSL Certificate.
           * You will be able to set up Security using these three lines of code.
           */

        LogClass.debug(new File("blah.txt").getAbsolutePath());
        System.setProperty("axis.ClientConfigFile", "client-config.wsdd");

        //Sets up the Amazon FPS Service Port from the URL endpoint provided to you.

        URL endpoint = new URL("https://fps.sandbox.amazonaws.com/");

        AmazonFPSPortType amazonFPSPort = new AmazonFPSLocator().getAmazonFPSPort(endpoint);
        /*---------------------------- End SETUP --------------------------------*/
        return amazonFPSPort;
    }

    public static Transaction pay(String recipientID, String callerID, String senderID, String amount, String callerReference) throws ServiceException,
            RemoteException, MalformedURLException {

        //Do a setup.
        AmazonFPSPortType amazonFPSPort = setup();

        /*---------------------------- Begin construction of Pay Request --------------------------------*/
        //Creating a 'PayRequest' object
        PayRequest payRequest = new PayRequest();

        /*________Set Required Parameters.________*/
        /* Specify the Caller, Recipient and Sender Tokens here.
           * In this Example, Caller and Recipient Tokens are installed on DigitalDownload's Amazon Payments
           * using InstallPaymentInstruction API Call.Sender Token is installed on John's Amazon Payments and
           * acquired by DigitalDownload.
           * */
        payRequest.setCallerTokenId(callerID);
        payRequest.setRecipientTokenId(recipientID);
        payRequest.setSenderTokenId(senderID);
        //Transaction Amount of $0.10 for the Mp3 Requested by John.
        payRequest.setTransactionAmount(new Amount(CurrencyCode.USD, amount));
        /* Pay is idempotent on CallerReference. Make sure that you pass a unique CallerReference for
           * each transaction. This CallerReference represents your business logic to retrieve transactions.
           * In this use-case, this is the unique DownloadId for this particular download.
           * */
        payRequest.setCallerReference(callerReference);
        /* Set the ChargeFeeTo parameter which defines how the fees will be distributed.
           * Amazon FPS Fees can be paid either by the Caller or the Recipient.
           * In this use-case, since caller=recipient, it doesn't matter.
           */
        payRequest.setChargeFeeTo(ChargeFeeTo.Caller);
        /*	The caller can specify a transaction date that will be saved
           along with the transaction as shown below.	*/
        payRequest.setTransactionDate(Calendar.getInstance());

        /*________Set Optional Parameters.________*/
        /* CallerDescription can be set to give additional information about the caller. */
        //payRequest.setCallerDescription("Digital Content Download.");
        /* Set MetaData which is a free form field to store 2KB of transaction related data*/
        //payRequest.setMetaData("Digital Content downloaded: Wonderful Tonight - Eric Clapton");
        /*Recipient Related Information.
           * In this use-case, it doesn't make sense to set Recipient information because caller=recipient
           * */
        //payRequest.setRecipientDescription("");
        //payRequest.setRecipientReference(""+Calendar.getInstance().getTimeInMillis());
        /* Sender Related Information
           * In this use-case, Sender Description reflects the name of the sender and sender reference reflects the user id of the sender.
           * */
        //payRequest.setSenderDescription("John");
        //payRequest.setSenderReference("DIGUSR12"+Calendar.getInstance().getTimeInMillis());

        /*---------------------------- End Construct PayRequest --------------------------------*/

        //	This is the call to the 'Pay' Operation.
        PayResponse payResponse = amazonFPSPort.pay(payRequest);

        Transaction transaction = new Transaction();

        /*---------------------------- Start Handling PayResponse --------------------------------*/
        // Checking the status returned by the 'pay' API call.
        if (payResponse.getStatus().getValue().equals(ResponseStatus._Success)) {
            /*_________Handling Pay Operation Success Case_________*/
            //Get the Transaction response.
            TransactionResponse transactionResponse = payResponse.getTransactionResponse();
            //Get the TransactionId for the Pay Operation Executed.
            String transactionId = transactionResponse.getTransactionId();

            transaction.setTransactionID(transactionId);

            /* This Sample Code prints only the TransactionId and a message.
                * You can take some action like storing the Transaction details, marking your Db etc. according to your business logic.
                */
            /*---------------------------- Handle Syncronous Response --------------------------------*/
            if (transactionResponse.getStatus().getValue().equals(TransactionStatus._Success)) {
                /* Transaction Status Success. Instant success happens in Account Balance Transfer where the money moves
                     * from the Sender's Amazon Payments to the Recipient's Amazon Payments.
                     */
                LogClass.debug("Pay Succeeded and the money has been transferred from the Sender's Payment Instrument to the Recipient's Amazon Payments.");
                LogClass.debug(transactionResponse.getStatusDetail());
                // This Sample code prints the TransactionId only. You can get the Transaction Details by using GetTransaction Operation.
                LogClass.debug("TransactionId: " + transactionId);
            }
            if (transactionResponse.getStatus().getValue().equals(TransactionStatus._TemporaryDecline)) {
                /* Transaction Status RetriableFailure. This happens due to some locks on the accounts or tokens used in the Pay Operation.
                     * You can Retry the Transaction using a background thread using the RetryTransaction Operation.
                     */
                LogClass.debug("Retry after some time for the pay to succeed!");
                LogClass.debug(transactionResponse.getStatusDetail());
                LogClass.debug("Use this TransactionId \"" + transactionId + "\" in the RetryTransaction Operation.");
            }
            /*---------------------------- Handle Asynchronous Response--------------------------------*/
            if (transactionResponse.getStatus().getValue().equals(TransactionStatus._Initiated)) {
                /* Transaction Status Initiated. This happens when Amazon FPS has initiated a transaction, but will take some time to complete.
                     * This happens when the Sender uses either a Credit Card or a Bank Account to make a payment. Normally Credit Card transactions
                     * take 6-7 seconds to complete and Bank Account Transactions take about 4 days to complete.
                     */
                //You can use either GetResults\DiscardResults or Caller Notifications to poll for the Transaction Status.
                LogClass.debug("Transaction Initiated. Poll for results to know the Transaction Status.");
                LogClass.debug(transactionResponse.getStatusDetail());
                LogClass.debug("TransactionId: " + transactionId);
            }
            if (transactionResponse.getStatus().getValue().equals(TransactionStatus._Failure)) {
                /* Transaction Status Failure. This happens only when Transaction Status was Initiated sometime back.
                     */
                LogClass.debug("Transaction Failed");
                LogClass.debug(transactionResponse.getStatusDetail());
                //In this case you can execute your business logic like stop shipping the product etc.
            }

        } else {
            /*_________Handling Pay Operation Failure Case_________*/
            //Note that handling pay operation failure case is Synchronous.
            ServiceError[] serviceErrors = payResponse.getErrors();
            LogClass.debug("Following errors occured in the Pay API Call.");
            for (int i = 0; i < serviceErrors.length; i++) {
                LogClass.debug("There is some Business error in the Pay Operation Call. "
                        + "Refer to the ErrorCode and ReasonText for specific information.");
                LogClass.debug("ErrorCode: " + payResponse.getErrors()[0].getErrorCode());
                LogClass.debug("ReasonText: " + payResponse.getErrors()[0].getReasonText());
            }
        }
        /*---------------------------- End Handling PayResponse --------------------------------*/
        return transaction;
    }

    public void updateTransactions(Map<String, Transaction> transactions) throws RemoteException, MalformedURLException, ServiceException {
        AmazonFPSPortType afps = setup();
        GetResultsRequest getResultsRequest = new GetResultsRequest();

        /*________Set Required Parameters.________*/
        /*  Set the maximum number of results. Max value for this field is 24 */
        getResultsRequest.setMaxResultsCount(new BigInteger("24"));

        /* This is the call to the 'GetResults' Operation.*/
        GetResultsResponse getResultsResponse = afps.getResults(getResultsRequest);

        /*----------------- Begin Process GetResults Response -----------------*/
        /*  Check the status returned by the 'getResults' API call. */
        if (getResultsResponse.getStatus().getValue().equals(ResponseStatus._Success)) {
            /*_________Handling GetResults Operation Success Case_________*/
            TransactionResult[] transactionResults = getResultsResponse.getTransactionResults();
            if (transactionResults != null) {

                /* Initializing an array to store the transaction ids which have to be discarded later.
                *
                * NOTE: The code below makes a list of all the transaction results received. If there are
                * more than 24 old undiscarded results, then your new transaction results might not be
                * returned until you discard the old results. So, below all the old results of this caller
                * account are discarded until the required result (of the current transaction) is returned.
                *
                * IMPORTANT: Before discarding all the old results make sure that you will not be needing them
                * in future.
                *
                */
                String[] discardTransactionIds = new String[transactionResults.length];
                for (int i = 0; i < transactionResults.length; i++) {

                    String transactionID = transactionResults[i].getTransactionId();

                    Transaction transaction = transactions.get(transactionID);

                    discardTransactionIds[i] = transactionResults[i].getTransactionId();



                    //if (transactionResults[i].getTransactionId().equals(transactionId)) {
                        // Search for the required Transaction ID in the results returned by the
                        // Get Results API.
                        boolean callGetResults = false;
                        boolean resultFound = true;
                        String transactionStatus = transactionResults[i].getStatus().getValue();
                        //log.info("GetResults API call successful for Transaction ID: " + transactionId);
                        if (transactionStatus.equals(TransactionStatus._Success)) {
                            /* Transaction Status Success. Instant success happens in Account Balance Transfer where the money moves
                            * from the Sender's Amazon FPS Account to the Recipient's Amazon FPS Account.
                            * We will have Instant transfers from Credit Card\Bank Account to Amazon FPS Account in future.
                            */
                            //log.info("Pay Succeeded and the money has been transferred from the Sender's Payment Instrument to the Recipient's Amazon FPS Account.");

                            // This Sample code logs the TransactionId only. You can get the Transaction Details by using GetTransaction Operation.

                            // log.info("TransactionId: " + transactionId);
                        } else if (transactionStatus.equals(TransactionStatus._TemporaryDecline)) {
                            /* Transaction Status TemporaryDecline. This happens due to some locks on the accounts or tokens used in the Pay Operation.
                            * You can Retry the Transaction using a background thread using the RetryTransaction Operation.
                            */
                            /*error = "Pay request has been declined temporarily. Please retry after some time for Pay to succeed!<br>";
                            error += "<br>TransactionId: " + transactionId;
                            error += "<br>Please refer to the developer documentation for more information about RetryTransaction API.";

                            errorMessages += "Your transaction has been declined temporarily. Please check the status later by visiting " + "your account section of this web site.";*/
                        } else if (transactionStatus.equals(TransactionStatus._Failure)) {
                            /* Transaction Status Failure. This happens only when Transaction Status was Initiated sometime back.
                            * It might fail due to various reasons like "not enough balance in the Bank Account" etc.
                            * You get this status by polling for a transaction.
                            */
                            /*error = "Pay Transaction Status: " + transactionStatus; //+ " .<br> Please click on the link below to proceed to the home page.";
                            error += "<br>TransactionId: " + transactionId;

                            //In this case you can execute your business logic like stop shipping the product etc.
                            errorMessages += "Your transaction has been declined.";*/
                        }
                        /* In case the required transaction result (of the current transaction) is received,
                        * there is no need to discard all the old results. So the array below is reinitialized
                        * such that only the current transaction's result will be discarded.
                        *
                        */
                        discardTransactionIds = new String[]{transactionID};
                        break;
                    //}
                }
            }

        } else {
            //callGetResults = false;

            /*_________Handling GetResults Operation Failure Case_________*/
            /*ServiceError[] serviceErrors = getResultsResponse.getErrors().getErrors();
            error = "Following errors occured in the GetResults API Call. ";
            for (int i = 0; i < serviceErrors.length; i++) {
                error += "<br><br>ErrorType: " + serviceErrors[i].getErrorType().getValue();
                error += "<br>ErrorCode: " + serviceErrors[i].getErrorCode();
                error += "<br>ReasonText: " + serviceErrors[i].getReasonText();
            }
            error += "<br><br><i><span class=\"note\">These are the error codes returned by Amazon FPS. " + "You can map them to your business logic.</span></i>";*/
        }
        /*----------------- End Processing of GetResults Response -----------------*/
    }
}
