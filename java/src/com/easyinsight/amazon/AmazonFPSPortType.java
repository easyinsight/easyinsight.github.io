/**
 * AmazonFPSPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public interface AmazonFPSPortType extends java.rmi.Remote {
    public com.easyinsight.amazon.InstallPaymentInstructionBatchResponse installPaymentInstructionBatch(com.easyinsight.amazon.InstallPaymentInstructionBatchRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.InstallPaymentInstructionResponse installPaymentInstruction(com.easyinsight.amazon.InstallPaymentInstructionRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.GetTokenUsageResponse getTokenUsage(com.easyinsight.amazon.GetTokenUsageRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.GetPrepaidBalanceResponse getPrepaidBalance(com.easyinsight.amazon.GetPrepaidBalanceRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.GetDebtBalanceResponse getDebtBalance(com.easyinsight.amazon.GetDebtBalanceRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.GetTotalPrepaidLiabilityResponse getTotalPrepaidLiability(com.easyinsight.amazon.GetTotalPrepaidLiabilityRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.GetOutstandingDebtBalanceResponse getOutstandingDebtBalance(com.easyinsight.amazon.GetOutstandingDebtBalanceRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.SettleDebtResponse settleDebt(com.easyinsight.amazon.SettleDebtRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.WriteOffDebtResponse writeOffDebt(com.easyinsight.amazon.WriteOffDebtRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.FundPrepaidResponse fundPrepaid(com.easyinsight.amazon.FundPrepaidRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.SubscribeForCallerNotificationResponse subscribeForCallerNotification(com.easyinsight.amazon.SubscribeForCallerNotificationRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.UnSubscribeForCallerNotificationResponse unSubscribeForCallerNotification(com.easyinsight.amazon.UnSubscribeForCallerNotificationRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.GetAllCreditInstrumentsResponse getAllCreditInstruments(com.easyinsight.amazon.GetAllCreditInstrumentsRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.GetAllPrepaidInstrumentsResponse getAllPrepaidInstruments(com.easyinsight.amazon.GetAllPrepaidInstrumentsRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.GetAccountBalanceResponse getAccountBalance(com.easyinsight.amazon.GetAccountBalanceRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.GetAccountActivityResponse getAccountActivity(com.easyinsight.amazon.GetAccountActivityRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.GetTokensResponse getTokens(com.easyinsight.amazon.GetTokensRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.GetPaymentInstructionResponse getPaymentInstruction(com.easyinsight.amazon.GetPaymentInstructionRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.CancelTokenResponse cancelToken(com.easyinsight.amazon.CancelTokenRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.PayBatchResponse payBatch(com.easyinsight.amazon.PayBatchRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.PayResponse pay(com.easyinsight.amazon.PayRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.ReserveResponse reserve(com.easyinsight.amazon.ReserveRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.SettleResponse settle(com.easyinsight.amazon.SettleRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.RefundResponse refund(com.easyinsight.amazon.RefundRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.GetResultsResponse getResults(com.easyinsight.amazon.GetResultsRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.DiscardResultsResponse discardResults(com.easyinsight.amazon.DiscardResultsRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.GetTransactionResponse getTransaction(com.easyinsight.amazon.GetTransactionRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.RetryTransactionResponse retryTransaction(com.easyinsight.amazon.RetryTransactionRequest body) throws java.rmi.RemoteException;
    public com.easyinsight.amazon.GetTokenByCallerResponse getTokenByCaller(com.easyinsight.amazon.GetTokenByCallerRequest body) throws java.rmi.RemoteException;
}
