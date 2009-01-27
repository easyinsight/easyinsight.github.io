/**
 * AmazonFPSBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class AmazonFPSBindingStub extends org.apache.axis.client.Stub implements com.easyinsight.amazon.AmazonFPSPortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[29];
        _initOperationDesc1();
        _initOperationDesc2();
        _initOperationDesc3();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("InstallPaymentInstructionBatch");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "InstallPaymentInstructionBatchRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">InstallPaymentInstructionBatchRequest"), com.easyinsight.amazon.InstallPaymentInstructionBatchRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">InstallPaymentInstructionBatchResponse"));
        oper.setReturnClass(com.easyinsight.amazon.InstallPaymentInstructionBatchResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "InstallPaymentInstructionBatchResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("InstallPaymentInstruction");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "InstallPaymentInstructionRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">InstallPaymentInstructionRequest"), com.easyinsight.amazon.InstallPaymentInstructionRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">InstallPaymentInstructionResponse"));
        oper.setReturnClass(com.easyinsight.amazon.InstallPaymentInstructionResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "InstallPaymentInstructionResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetTokenUsage");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetTokenUsageRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetTokenUsageRequest"), com.easyinsight.amazon.GetTokenUsageRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetTokenUsageResponse"));
        oper.setReturnClass(com.easyinsight.amazon.GetTokenUsageResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetTokenUsageResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetPrepaidBalance");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetPrepaidBalanceRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetPrepaidBalanceRequest"), com.easyinsight.amazon.GetPrepaidBalanceRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetPrepaidBalanceResponse"));
        oper.setReturnClass(com.easyinsight.amazon.GetPrepaidBalanceResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetPrepaidBalanceResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetDebtBalance");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetDebtBalanceRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetDebtBalanceRequest"), com.easyinsight.amazon.GetDebtBalanceRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetDebtBalanceResponse"));
        oper.setReturnClass(com.easyinsight.amazon.GetDebtBalanceResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetDebtBalanceResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetTotalPrepaidLiability");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetTotalPrepaidLiabilityRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetTotalPrepaidLiabilityRequest"), com.easyinsight.amazon.GetTotalPrepaidLiabilityRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetTotalPrepaidLiabilityResponse"));
        oper.setReturnClass(com.easyinsight.amazon.GetTotalPrepaidLiabilityResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetTotalPrepaidLiabilityResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetOutstandingDebtBalance");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetOutstandingDebtBalanceRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetOutstandingDebtBalanceRequest"), com.easyinsight.amazon.GetOutstandingDebtBalanceRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetOutstandingDebtBalanceResponse"));
        oper.setReturnClass(com.easyinsight.amazon.GetOutstandingDebtBalanceResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetOutstandingDebtBalanceResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SettleDebt");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "SettleDebtRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">SettleDebtRequest"), com.easyinsight.amazon.SettleDebtRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">SettleDebtResponse"));
        oper.setReturnClass(com.easyinsight.amazon.SettleDebtResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "SettleDebtResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("WriteOffDebt");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "WriteOffDebtRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">WriteOffDebtRequest"), com.easyinsight.amazon.WriteOffDebtRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">WriteOffDebtResponse"));
        oper.setReturnClass(com.easyinsight.amazon.WriteOffDebtResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "WriteOffDebtResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("FundPrepaid");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "FundPrepaidRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">FundPrepaidRequest"), com.easyinsight.amazon.FundPrepaidRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">FundPrepaidResponse"));
        oper.setReturnClass(com.easyinsight.amazon.FundPrepaidResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "FundPrepaidResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SubscribeForCallerNotification");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "SubscribeForCallerNotificationRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">SubscribeForCallerNotificationRequest"), com.easyinsight.amazon.SubscribeForCallerNotificationRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">SubscribeForCallerNotificationResponse"));
        oper.setReturnClass(com.easyinsight.amazon.SubscribeForCallerNotificationResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "SubscribeForCallerNotificationResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UnSubscribeForCallerNotification");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "UnSubscribeForCallerNotificationRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">UnSubscribeForCallerNotificationRequest"), com.easyinsight.amazon.UnSubscribeForCallerNotificationRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">UnSubscribeForCallerNotificationResponse"));
        oper.setReturnClass(com.easyinsight.amazon.UnSubscribeForCallerNotificationResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "UnSubscribeForCallerNotificationResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetAllCreditInstruments");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetAllCreditInstrumentsRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetAllCreditInstrumentsRequest"), com.easyinsight.amazon.GetAllCreditInstrumentsRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetAllCreditInstrumentsResponse"));
        oper.setReturnClass(com.easyinsight.amazon.GetAllCreditInstrumentsResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetAllCreditInstrumentsResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[12] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetAllPrepaidInstruments");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetAllPrepaidInstrumentsRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetAllPrepaidInstrumentsRequest"), com.easyinsight.amazon.GetAllPrepaidInstrumentsRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetAllPrepaidInstrumentsResponse"));
        oper.setReturnClass(com.easyinsight.amazon.GetAllPrepaidInstrumentsResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetAllPrepaidInstrumentsResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[13] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetAccountBalance");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetAccountBalanceRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetAccountBalanceRequest"), com.easyinsight.amazon.GetAccountBalanceRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetAccountBalanceResponse"));
        oper.setReturnClass(com.easyinsight.amazon.GetAccountBalanceResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetAccountBalanceResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[14] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetAccountActivity");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetAccountActivityRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetAccountActivityRequest"), com.easyinsight.amazon.GetAccountActivityRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetAccountActivityResponse"));
        oper.setReturnClass(com.easyinsight.amazon.GetAccountActivityResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetAccountActivityResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[15] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetTokens");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetTokensRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetTokensRequest"), com.easyinsight.amazon.GetTokensRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetTokensResponse"));
        oper.setReturnClass(com.easyinsight.amazon.GetTokensResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetTokensResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[16] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetPaymentInstruction");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetPaymentInstructionRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetPaymentInstructionRequest"), com.easyinsight.amazon.GetPaymentInstructionRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetPaymentInstructionResponse"));
        oper.setReturnClass(com.easyinsight.amazon.GetPaymentInstructionResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetPaymentInstructionResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[17] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CancelToken");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "CancelTokenRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">CancelTokenRequest"), com.easyinsight.amazon.CancelTokenRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">CancelTokenResponse"));
        oper.setReturnClass(com.easyinsight.amazon.CancelTokenResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "CancelTokenResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[18] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("PayBatch");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "PayBatchRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">PayBatchRequest"), com.easyinsight.amazon.PayBatchRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">PayBatchResponse"));
        oper.setReturnClass(com.easyinsight.amazon.PayBatchResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "PayBatchResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[19] = oper;

    }

    private static void _initOperationDesc3(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("Pay");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "PayRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">PayRequest"), com.easyinsight.amazon.PayRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">PayResponse"));
        oper.setReturnClass(com.easyinsight.amazon.PayResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "PayResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[20] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("Reserve");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "ReserveRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">ReserveRequest"), com.easyinsight.amazon.ReserveRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">ReserveResponse"));
        oper.setReturnClass(com.easyinsight.amazon.ReserveResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "ReserveResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[21] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("Settle");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "SettleRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">SettleRequest"), com.easyinsight.amazon.SettleRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">SettleResponse"));
        oper.setReturnClass(com.easyinsight.amazon.SettleResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "SettleResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[22] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("Refund");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "RefundRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">RefundRequest"), com.easyinsight.amazon.RefundRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">RefundResponse"));
        oper.setReturnClass(com.easyinsight.amazon.RefundResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "RefundResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[23] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetResults");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetResultsRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetResultsRequest"), com.easyinsight.amazon.GetResultsRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetResultsResponse"));
        oper.setReturnClass(com.easyinsight.amazon.GetResultsResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetResultsResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[24] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DiscardResults");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "DiscardResultsRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">DiscardResultsRequest"), com.easyinsight.amazon.DiscardResultsRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">DiscardResultsResponse"));
        oper.setReturnClass(com.easyinsight.amazon.DiscardResultsResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "DiscardResultsResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[25] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetTransaction");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetTransactionRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetTransactionRequest"), com.easyinsight.amazon.GetTransactionRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetTransactionResponse"));
        oper.setReturnClass(com.easyinsight.amazon.GetTransactionResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetTransactionResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[26] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("RetryTransaction");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "RetryTransactionRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">RetryTransactionRequest"), com.easyinsight.amazon.RetryTransactionRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">RetryTransactionResponse"));
        oper.setReturnClass(com.easyinsight.amazon.RetryTransactionResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "RetryTransactionResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[27] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetTokenByCaller");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetTokenByCallerRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetTokenByCallerRequest"), com.easyinsight.amazon.GetTokenByCallerRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetTokenByCallerResponse"));
        oper.setReturnClass(com.easyinsight.amazon.GetTokenByCallerResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetTokenByCallerResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[28] = oper;

    }

    public AmazonFPSBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public AmazonFPSBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public AmazonFPSBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.1");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">CancelTokenRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.CancelTokenRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">CancelTokenResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.CancelTokenResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">DiscardResultsRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.DiscardResultsRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">DiscardResultsResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.DiscardResultsResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">FundPrepaidRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.FundPrepaidRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">FundPrepaidResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.FundPrepaidResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetAccountActivityRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetAccountActivityRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetAccountActivityResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetAccountActivityResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetAccountBalanceRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetAccountBalanceRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetAccountBalanceResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetAccountBalanceResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetAllCreditInstrumentsRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetAllCreditInstrumentsRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetAllCreditInstrumentsResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetAllCreditInstrumentsResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetAllPrepaidInstrumentsRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetAllPrepaidInstrumentsRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetAllPrepaidInstrumentsResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetAllPrepaidInstrumentsResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetDebtBalanceRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetDebtBalanceRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetDebtBalanceResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetDebtBalanceResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetOutstandingDebtBalanceRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetOutstandingDebtBalanceRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetOutstandingDebtBalanceResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetOutstandingDebtBalanceResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetPaymentInstructionRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetPaymentInstructionRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetPaymentInstructionResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetPaymentInstructionResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetPrepaidBalanceRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetPrepaidBalanceRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetPrepaidBalanceResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetPrepaidBalanceResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetResultsRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetResultsRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetResultsResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetResultsResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetTokenByCallerRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetTokenByCallerRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetTokenByCallerResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetTokenByCallerResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetTokensRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetTokensRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetTokensResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetTokensResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetTokenUsageRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetTokenUsageRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetTokenUsageResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetTokenUsageResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetTotalPrepaidLiabilityRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetTotalPrepaidLiabilityRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetTotalPrepaidLiabilityResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetTotalPrepaidLiabilityResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetTransactionRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetTransactionRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetTransactionResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetTransactionResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">InstallPaymentInstructionBatchRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.InstallPaymentInstructionBatchRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">InstallPaymentInstructionBatchResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.InstallPaymentInstructionBatchResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">InstallPaymentInstructionRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.InstallPaymentInstructionRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">InstallPaymentInstructionResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.InstallPaymentInstructionResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">PayBatchRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.PayBatchRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">PayBatchResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.PayBatchResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">PayRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.PayRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">PayResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.PayResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">RefundRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.RefundRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">RefundResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.RefundResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">ReserveRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.ReserveRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">ReserveResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.ReserveResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">RetryTransactionRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.RetryTransactionRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">RetryTransactionResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.RetryTransactionResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">SettleDebtRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.SettleDebtRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">SettleDebtResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.SettleDebtResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">SettleRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.SettleRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">SettleResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.SettleResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">SubscribeForCallerNotificationRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.SubscribeForCallerNotificationRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">SubscribeForCallerNotificationResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.SubscribeForCallerNotificationResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">TransactionResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.TransactionResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">UnSubscribeForCallerNotificationRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.UnSubscribeForCallerNotificationRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">UnSubscribeForCallerNotificationResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.UnSubscribeForCallerNotificationResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">WriteOffDebtRequest");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.WriteOffDebtRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">WriteOffDebtResponse");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.WriteOffDebtResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "AccountBalance");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.AccountBalance.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "AccountId");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "Amount");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.Amount.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "AvailableBalances");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.AvailableBalances.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "ChargeFeeTo");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.ChargeFeeTo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "CurrencyCode");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.CurrencyCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "DebtBalance");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.DebtBalance.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "ErrorType");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.ErrorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "FPSOperation");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.FPSOperation.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "FPSOperationFilter");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.FPSOperationFilter.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "GetAccountActivityResponseGroup");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.GetAccountActivityResponseGroup.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "InstrumentId");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "InstrumentStatus");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.InstrumentStatus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "MarketplaceRefundPolicy");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.MarketplaceRefundPolicy.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "OutstandingDebtBalance");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.OutstandingDebtBalance.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "OutstandingPrepaidLiability");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.OutstandingPrepaidLiability.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "PaymentMethod");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.PaymentMethod.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "PrepaidBalance");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.PrepaidBalance.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "RelatedTransaction");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.RelatedTransaction.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "ResponseStatus");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.ResponseStatus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "ServiceError");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.ServiceError.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "ServiceErrors");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.ServiceError[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "ServiceError");
            qName2 = new javax.xml.namespace.QName("", "Errors");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "SortOrder");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.SortOrder.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "StatusChange");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.StatusChange.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "Token");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.Token.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TokenId");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TokenStatus");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.TokenStatus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TokenType");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.TokenType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TokenUsageLimit");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.TokenUsageLimit.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "Transaction");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.Transaction.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TransactionalRole");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.TransactionalRole.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TransactionalRoleFilter");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.TransactionalRoleFilter.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TransactionDetail");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.TransactionDetail.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TransactionPart");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.TransactionPart.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TransactionResult");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.TransactionResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TransactionResultStatus");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.TransactionResultStatus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TransactionStatus");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.TransactionStatus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TransactionStatusFilter");
            cachedSerQNames.add(qName);
            cls = com.easyinsight.amazon.TransactionStatusFilter.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public com.easyinsight.amazon.InstallPaymentInstructionBatchResponse installPaymentInstructionBatch(com.easyinsight.amazon.InstallPaymentInstructionBatchRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:InstallPaymentInstructionBatch");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "InstallPaymentInstructionBatch"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.InstallPaymentInstructionBatchResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.InstallPaymentInstructionBatchResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.InstallPaymentInstructionBatchResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.InstallPaymentInstructionResponse installPaymentInstruction(com.easyinsight.amazon.InstallPaymentInstructionRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:InstallPaymentInstruction");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "InstallPaymentInstruction"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.InstallPaymentInstructionResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.InstallPaymentInstructionResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.InstallPaymentInstructionResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.GetTokenUsageResponse getTokenUsage(com.easyinsight.amazon.GetTokenUsageRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:GetTokenUsage");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetTokenUsage"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.GetTokenUsageResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.GetTokenUsageResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.GetTokenUsageResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.GetPrepaidBalanceResponse getPrepaidBalance(com.easyinsight.amazon.GetPrepaidBalanceRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:GetPrepaidBalance");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetPrepaidBalance"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.GetPrepaidBalanceResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.GetPrepaidBalanceResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.GetPrepaidBalanceResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.GetDebtBalanceResponse getDebtBalance(com.easyinsight.amazon.GetDebtBalanceRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:GetDebtBalance");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetDebtBalance"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.GetDebtBalanceResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.GetDebtBalanceResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.GetDebtBalanceResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.GetTotalPrepaidLiabilityResponse getTotalPrepaidLiability(com.easyinsight.amazon.GetTotalPrepaidLiabilityRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:GetTotalPrepaidLiability");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetTotalPrepaidLiability"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.GetTotalPrepaidLiabilityResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.GetTotalPrepaidLiabilityResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.GetTotalPrepaidLiabilityResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.GetOutstandingDebtBalanceResponse getOutstandingDebtBalance(com.easyinsight.amazon.GetOutstandingDebtBalanceRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:GetOutstandingDebtBalance");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetOutstandingDebtBalance"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.GetOutstandingDebtBalanceResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.GetOutstandingDebtBalanceResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.GetOutstandingDebtBalanceResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.SettleDebtResponse settleDebt(com.easyinsight.amazon.SettleDebtRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:SettleDebt");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "SettleDebt"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.SettleDebtResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.SettleDebtResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.SettleDebtResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.WriteOffDebtResponse writeOffDebt(com.easyinsight.amazon.WriteOffDebtRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:WriteOffDebt");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "WriteOffDebt"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.WriteOffDebtResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.WriteOffDebtResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.WriteOffDebtResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.FundPrepaidResponse fundPrepaid(com.easyinsight.amazon.FundPrepaidRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:FundPrepaid");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "FundPrepaid"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.FundPrepaidResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.FundPrepaidResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.FundPrepaidResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.SubscribeForCallerNotificationResponse subscribeForCallerNotification(com.easyinsight.amazon.SubscribeForCallerNotificationRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:SubscribeForCallerNotification");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "SubscribeForCallerNotification"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.SubscribeForCallerNotificationResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.SubscribeForCallerNotificationResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.SubscribeForCallerNotificationResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.UnSubscribeForCallerNotificationResponse unSubscribeForCallerNotification(com.easyinsight.amazon.UnSubscribeForCallerNotificationRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:UnSubscribeForCallerNotification");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "UnSubscribeForCallerNotification"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.UnSubscribeForCallerNotificationResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.UnSubscribeForCallerNotificationResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.UnSubscribeForCallerNotificationResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.GetAllCreditInstrumentsResponse getAllCreditInstruments(com.easyinsight.amazon.GetAllCreditInstrumentsRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:GetAllCreditInstruments");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetAllCreditInstruments"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.GetAllCreditInstrumentsResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.GetAllCreditInstrumentsResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.GetAllCreditInstrumentsResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.GetAllPrepaidInstrumentsResponse getAllPrepaidInstruments(com.easyinsight.amazon.GetAllPrepaidInstrumentsRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:GetAllPrepaidInstruments");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetAllPrepaidInstruments"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.GetAllPrepaidInstrumentsResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.GetAllPrepaidInstrumentsResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.GetAllPrepaidInstrumentsResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.GetAccountBalanceResponse getAccountBalance(com.easyinsight.amazon.GetAccountBalanceRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[14]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:GetAccountBalance");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetAccountBalance"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.GetAccountBalanceResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.GetAccountBalanceResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.GetAccountBalanceResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.GetAccountActivityResponse getAccountActivity(com.easyinsight.amazon.GetAccountActivityRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[15]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:GetAccountActivity");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetAccountActivity"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.GetAccountActivityResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.GetAccountActivityResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.GetAccountActivityResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.GetTokensResponse getTokens(com.easyinsight.amazon.GetTokensRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[16]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:GetTokens");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetTokens"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.GetTokensResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.GetTokensResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.GetTokensResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.GetPaymentInstructionResponse getPaymentInstruction(com.easyinsight.amazon.GetPaymentInstructionRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[17]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:GetPaymentInstruction");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetPaymentInstruction"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.GetPaymentInstructionResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.GetPaymentInstructionResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.GetPaymentInstructionResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.CancelTokenResponse cancelToken(com.easyinsight.amazon.CancelTokenRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[18]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:CancelToken");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "CancelToken"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.CancelTokenResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.CancelTokenResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.CancelTokenResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.PayBatchResponse payBatch(com.easyinsight.amazon.PayBatchRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[19]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:PayBatch");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "PayBatch"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.PayBatchResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.PayBatchResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.PayBatchResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.PayResponse pay(com.easyinsight.amazon.PayRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[20]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:Pay");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "Pay"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.PayResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.PayResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.PayResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.ReserveResponse reserve(com.easyinsight.amazon.ReserveRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[21]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:Reserve");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "Reserve"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.ReserveResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.ReserveResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.ReserveResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.SettleResponse settle(com.easyinsight.amazon.SettleRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[22]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:Settle");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "Settle"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.SettleResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.SettleResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.SettleResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.RefundResponse refund(com.easyinsight.amazon.RefundRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[23]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:Refund");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "Refund"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.RefundResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.RefundResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.RefundResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.GetResultsResponse getResults(com.easyinsight.amazon.GetResultsRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[24]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:GetResults");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetResults"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.GetResultsResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.GetResultsResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.GetResultsResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.DiscardResultsResponse discardResults(com.easyinsight.amazon.DiscardResultsRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[25]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:DiscardResults");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "DiscardResults"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.DiscardResultsResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.DiscardResultsResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.DiscardResultsResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.GetTransactionResponse getTransaction(com.easyinsight.amazon.GetTransactionRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[26]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:GetTransaction");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetTransaction"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.GetTransactionResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.GetTransactionResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.GetTransactionResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.RetryTransactionResponse retryTransaction(com.easyinsight.amazon.RetryTransactionRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[27]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:RetryTransaction");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "RetryTransaction"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.RetryTransactionResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.RetryTransactionResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.RetryTransactionResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.easyinsight.amazon.GetTokenByCallerResponse getTokenByCaller(com.easyinsight.amazon.GetTokenByCallerRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[28]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:GetTokenByCaller");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetTokenByCaller"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.easyinsight.amazon.GetTokenByCallerResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.easyinsight.amazon.GetTokenByCallerResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.easyinsight.amazon.GetTokenByCallerResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
