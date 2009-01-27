/**
 * TransactionDetail.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class TransactionDetail  implements java.io.Serializable {
    private java.lang.String transactionId;

    private java.util.Calendar callerTransactionDate;

    private java.util.Calendar dateReceived;

    private java.util.Calendar dateCompleted;

    private com.easyinsight.amazon.Amount transactionAmount;

    private com.easyinsight.amazon.Amount fees;

    private java.lang.String callerTokenId;

    private java.lang.String senderTokenId;

    private java.lang.String recipientTokenId;

    private java.lang.String prepaidInstrumentId;

    private java.lang.String creditInstrumentId;

    private com.easyinsight.amazon.FPSOperation operation;

    private com.easyinsight.amazon.PaymentMethod paymentMethod;

    private com.easyinsight.amazon.TransactionStatus status;

    private java.lang.String errorCode;

    private java.lang.String errorMessage;

    private java.lang.String metaData;

    private java.lang.String senderName;

    private java.lang.String callerName;

    private java.lang.String recipientName;

    private com.easyinsight.amazon.TransactionPart[] transactionParts;

    private com.easyinsight.amazon.RelatedTransaction[] relatedTransactions;

    private com.easyinsight.amazon.StatusChange[] statusHistory;

    public TransactionDetail() {
    }

    public TransactionDetail(
           java.lang.String transactionId,
           java.util.Calendar callerTransactionDate,
           java.util.Calendar dateReceived,
           java.util.Calendar dateCompleted,
           com.easyinsight.amazon.Amount transactionAmount,
           com.easyinsight.amazon.Amount fees,
           java.lang.String callerTokenId,
           java.lang.String senderTokenId,
           java.lang.String recipientTokenId,
           java.lang.String prepaidInstrumentId,
           java.lang.String creditInstrumentId,
           com.easyinsight.amazon.FPSOperation operation,
           com.easyinsight.amazon.PaymentMethod paymentMethod,
           com.easyinsight.amazon.TransactionStatus status,
           java.lang.String errorCode,
           java.lang.String errorMessage,
           java.lang.String metaData,
           java.lang.String senderName,
           java.lang.String callerName,
           java.lang.String recipientName,
           com.easyinsight.amazon.TransactionPart[] transactionParts,
           com.easyinsight.amazon.RelatedTransaction[] relatedTransactions,
           com.easyinsight.amazon.StatusChange[] statusHistory) {
           this.transactionId = transactionId;
           this.callerTransactionDate = callerTransactionDate;
           this.dateReceived = dateReceived;
           this.dateCompleted = dateCompleted;
           this.transactionAmount = transactionAmount;
           this.fees = fees;
           this.callerTokenId = callerTokenId;
           this.senderTokenId = senderTokenId;
           this.recipientTokenId = recipientTokenId;
           this.prepaidInstrumentId = prepaidInstrumentId;
           this.creditInstrumentId = creditInstrumentId;
           this.operation = operation;
           this.paymentMethod = paymentMethod;
           this.status = status;
           this.errorCode = errorCode;
           this.errorMessage = errorMessage;
           this.metaData = metaData;
           this.senderName = senderName;
           this.callerName = callerName;
           this.recipientName = recipientName;
           this.transactionParts = transactionParts;
           this.relatedTransactions = relatedTransactions;
           this.statusHistory = statusHistory;
    }


    /**
     * Gets the transactionId value for this TransactionDetail.
     * 
     * @return transactionId
     */
    public java.lang.String getTransactionId() {
        return transactionId;
    }


    /**
     * Sets the transactionId value for this TransactionDetail.
     * 
     * @param transactionId
     */
    public void setTransactionId(java.lang.String transactionId) {
        this.transactionId = transactionId;
    }


    /**
     * Gets the callerTransactionDate value for this TransactionDetail.
     * 
     * @return callerTransactionDate
     */
    public java.util.Calendar getCallerTransactionDate() {
        return callerTransactionDate;
    }


    /**
     * Sets the callerTransactionDate value for this TransactionDetail.
     * 
     * @param callerTransactionDate
     */
    public void setCallerTransactionDate(java.util.Calendar callerTransactionDate) {
        this.callerTransactionDate = callerTransactionDate;
    }


    /**
     * Gets the dateReceived value for this TransactionDetail.
     * 
     * @return dateReceived
     */
    public java.util.Calendar getDateReceived() {
        return dateReceived;
    }


    /**
     * Sets the dateReceived value for this TransactionDetail.
     * 
     * @param dateReceived
     */
    public void setDateReceived(java.util.Calendar dateReceived) {
        this.dateReceived = dateReceived;
    }


    /**
     * Gets the dateCompleted value for this TransactionDetail.
     * 
     * @return dateCompleted
     */
    public java.util.Calendar getDateCompleted() {
        return dateCompleted;
    }


    /**
     * Sets the dateCompleted value for this TransactionDetail.
     * 
     * @param dateCompleted
     */
    public void setDateCompleted(java.util.Calendar dateCompleted) {
        this.dateCompleted = dateCompleted;
    }


    /**
     * Gets the transactionAmount value for this TransactionDetail.
     * 
     * @return transactionAmount
     */
    public com.easyinsight.amazon.Amount getTransactionAmount() {
        return transactionAmount;
    }


    /**
     * Sets the transactionAmount value for this TransactionDetail.
     * 
     * @param transactionAmount
     */
    public void setTransactionAmount(com.easyinsight.amazon.Amount transactionAmount) {
        this.transactionAmount = transactionAmount;
    }


    /**
     * Gets the fees value for this TransactionDetail.
     * 
     * @return fees
     */
    public com.easyinsight.amazon.Amount getFees() {
        return fees;
    }


    /**
     * Sets the fees value for this TransactionDetail.
     * 
     * @param fees
     */
    public void setFees(com.easyinsight.amazon.Amount fees) {
        this.fees = fees;
    }


    /**
     * Gets the callerTokenId value for this TransactionDetail.
     * 
     * @return callerTokenId
     */
    public java.lang.String getCallerTokenId() {
        return callerTokenId;
    }


    /**
     * Sets the callerTokenId value for this TransactionDetail.
     * 
     * @param callerTokenId
     */
    public void setCallerTokenId(java.lang.String callerTokenId) {
        this.callerTokenId = callerTokenId;
    }


    /**
     * Gets the senderTokenId value for this TransactionDetail.
     * 
     * @return senderTokenId
     */
    public java.lang.String getSenderTokenId() {
        return senderTokenId;
    }


    /**
     * Sets the senderTokenId value for this TransactionDetail.
     * 
     * @param senderTokenId
     */
    public void setSenderTokenId(java.lang.String senderTokenId) {
        this.senderTokenId = senderTokenId;
    }


    /**
     * Gets the recipientTokenId value for this TransactionDetail.
     * 
     * @return recipientTokenId
     */
    public java.lang.String getRecipientTokenId() {
        return recipientTokenId;
    }


    /**
     * Sets the recipientTokenId value for this TransactionDetail.
     * 
     * @param recipientTokenId
     */
    public void setRecipientTokenId(java.lang.String recipientTokenId) {
        this.recipientTokenId = recipientTokenId;
    }


    /**
     * Gets the prepaidInstrumentId value for this TransactionDetail.
     * 
     * @return prepaidInstrumentId
     */
    public java.lang.String getPrepaidInstrumentId() {
        return prepaidInstrumentId;
    }


    /**
     * Sets the prepaidInstrumentId value for this TransactionDetail.
     * 
     * @param prepaidInstrumentId
     */
    public void setPrepaidInstrumentId(java.lang.String prepaidInstrumentId) {
        this.prepaidInstrumentId = prepaidInstrumentId;
    }


    /**
     * Gets the creditInstrumentId value for this TransactionDetail.
     * 
     * @return creditInstrumentId
     */
    public java.lang.String getCreditInstrumentId() {
        return creditInstrumentId;
    }


    /**
     * Sets the creditInstrumentId value for this TransactionDetail.
     * 
     * @param creditInstrumentId
     */
    public void setCreditInstrumentId(java.lang.String creditInstrumentId) {
        this.creditInstrumentId = creditInstrumentId;
    }


    /**
     * Gets the operation value for this TransactionDetail.
     * 
     * @return operation
     */
    public com.easyinsight.amazon.FPSOperation getOperation() {
        return operation;
    }


    /**
     * Sets the operation value for this TransactionDetail.
     * 
     * @param operation
     */
    public void setOperation(com.easyinsight.amazon.FPSOperation operation) {
        this.operation = operation;
    }


    /**
     * Gets the paymentMethod value for this TransactionDetail.
     * 
     * @return paymentMethod
     */
    public com.easyinsight.amazon.PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }


    /**
     * Sets the paymentMethod value for this TransactionDetail.
     * 
     * @param paymentMethod
     */
    public void setPaymentMethod(com.easyinsight.amazon.PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


    /**
     * Gets the status value for this TransactionDetail.
     * 
     * @return status
     */
    public com.easyinsight.amazon.TransactionStatus getStatus() {
        return status;
    }


    /**
     * Sets the status value for this TransactionDetail.
     * 
     * @param status
     */
    public void setStatus(com.easyinsight.amazon.TransactionStatus status) {
        this.status = status;
    }


    /**
     * Gets the errorCode value for this TransactionDetail.
     * 
     * @return errorCode
     */
    public java.lang.String getErrorCode() {
        return errorCode;
    }


    /**
     * Sets the errorCode value for this TransactionDetail.
     * 
     * @param errorCode
     */
    public void setErrorCode(java.lang.String errorCode) {
        this.errorCode = errorCode;
    }


    /**
     * Gets the errorMessage value for this TransactionDetail.
     * 
     * @return errorMessage
     */
    public java.lang.String getErrorMessage() {
        return errorMessage;
    }


    /**
     * Sets the errorMessage value for this TransactionDetail.
     * 
     * @param errorMessage
     */
    public void setErrorMessage(java.lang.String errorMessage) {
        this.errorMessage = errorMessage;
    }


    /**
     * Gets the metaData value for this TransactionDetail.
     * 
     * @return metaData
     */
    public java.lang.String getMetaData() {
        return metaData;
    }


    /**
     * Sets the metaData value for this TransactionDetail.
     * 
     * @param metaData
     */
    public void setMetaData(java.lang.String metaData) {
        this.metaData = metaData;
    }


    /**
     * Gets the senderName value for this TransactionDetail.
     * 
     * @return senderName
     */
    public java.lang.String getSenderName() {
        return senderName;
    }


    /**
     * Sets the senderName value for this TransactionDetail.
     * 
     * @param senderName
     */
    public void setSenderName(java.lang.String senderName) {
        this.senderName = senderName;
    }


    /**
     * Gets the callerName value for this TransactionDetail.
     * 
     * @return callerName
     */
    public java.lang.String getCallerName() {
        return callerName;
    }


    /**
     * Sets the callerName value for this TransactionDetail.
     * 
     * @param callerName
     */
    public void setCallerName(java.lang.String callerName) {
        this.callerName = callerName;
    }


    /**
     * Gets the recipientName value for this TransactionDetail.
     * 
     * @return recipientName
     */
    public java.lang.String getRecipientName() {
        return recipientName;
    }


    /**
     * Sets the recipientName value for this TransactionDetail.
     * 
     * @param recipientName
     */
    public void setRecipientName(java.lang.String recipientName) {
        this.recipientName = recipientName;
    }


    /**
     * Gets the transactionParts value for this TransactionDetail.
     * 
     * @return transactionParts
     */
    public com.easyinsight.amazon.TransactionPart[] getTransactionParts() {
        return transactionParts;
    }


    /**
     * Sets the transactionParts value for this TransactionDetail.
     * 
     * @param transactionParts
     */
    public void setTransactionParts(com.easyinsight.amazon.TransactionPart[] transactionParts) {
        this.transactionParts = transactionParts;
    }

    public com.easyinsight.amazon.TransactionPart getTransactionParts(int i) {
        return this.transactionParts[i];
    }

    public void setTransactionParts(int i, com.easyinsight.amazon.TransactionPart _value) {
        this.transactionParts[i] = _value;
    }


    /**
     * Gets the relatedTransactions value for this TransactionDetail.
     * 
     * @return relatedTransactions
     */
    public com.easyinsight.amazon.RelatedTransaction[] getRelatedTransactions() {
        return relatedTransactions;
    }


    /**
     * Sets the relatedTransactions value for this TransactionDetail.
     * 
     * @param relatedTransactions
     */
    public void setRelatedTransactions(com.easyinsight.amazon.RelatedTransaction[] relatedTransactions) {
        this.relatedTransactions = relatedTransactions;
    }

    public com.easyinsight.amazon.RelatedTransaction getRelatedTransactions(int i) {
        return this.relatedTransactions[i];
    }

    public void setRelatedTransactions(int i, com.easyinsight.amazon.RelatedTransaction _value) {
        this.relatedTransactions[i] = _value;
    }


    /**
     * Gets the statusHistory value for this TransactionDetail.
     * 
     * @return statusHistory
     */
    public com.easyinsight.amazon.StatusChange[] getStatusHistory() {
        return statusHistory;
    }


    /**
     * Sets the statusHistory value for this TransactionDetail.
     * 
     * @param statusHistory
     */
    public void setStatusHistory(com.easyinsight.amazon.StatusChange[] statusHistory) {
        this.statusHistory = statusHistory;
    }

    public com.easyinsight.amazon.StatusChange getStatusHistory(int i) {
        return this.statusHistory[i];
    }

    public void setStatusHistory(int i, com.easyinsight.amazon.StatusChange _value) {
        this.statusHistory[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TransactionDetail)) return false;
        TransactionDetail other = (TransactionDetail) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.transactionId==null && other.getTransactionId()==null) || 
             (this.transactionId!=null &&
              this.transactionId.equals(other.getTransactionId()))) &&
            ((this.callerTransactionDate==null && other.getCallerTransactionDate()==null) || 
             (this.callerTransactionDate!=null &&
              this.callerTransactionDate.equals(other.getCallerTransactionDate()))) &&
            ((this.dateReceived==null && other.getDateReceived()==null) || 
             (this.dateReceived!=null &&
              this.dateReceived.equals(other.getDateReceived()))) &&
            ((this.dateCompleted==null && other.getDateCompleted()==null) || 
             (this.dateCompleted!=null &&
              this.dateCompleted.equals(other.getDateCompleted()))) &&
            ((this.transactionAmount==null && other.getTransactionAmount()==null) || 
             (this.transactionAmount!=null &&
              this.transactionAmount.equals(other.getTransactionAmount()))) &&
            ((this.fees==null && other.getFees()==null) || 
             (this.fees!=null &&
              this.fees.equals(other.getFees()))) &&
            ((this.callerTokenId==null && other.getCallerTokenId()==null) || 
             (this.callerTokenId!=null &&
              this.callerTokenId.equals(other.getCallerTokenId()))) &&
            ((this.senderTokenId==null && other.getSenderTokenId()==null) || 
             (this.senderTokenId!=null &&
              this.senderTokenId.equals(other.getSenderTokenId()))) &&
            ((this.recipientTokenId==null && other.getRecipientTokenId()==null) || 
             (this.recipientTokenId!=null &&
              this.recipientTokenId.equals(other.getRecipientTokenId()))) &&
            ((this.prepaidInstrumentId==null && other.getPrepaidInstrumentId()==null) || 
             (this.prepaidInstrumentId!=null &&
              this.prepaidInstrumentId.equals(other.getPrepaidInstrumentId()))) &&
            ((this.creditInstrumentId==null && other.getCreditInstrumentId()==null) || 
             (this.creditInstrumentId!=null &&
              this.creditInstrumentId.equals(other.getCreditInstrumentId()))) &&
            ((this.operation==null && other.getOperation()==null) || 
             (this.operation!=null &&
              this.operation.equals(other.getOperation()))) &&
            ((this.paymentMethod==null && other.getPaymentMethod()==null) || 
             (this.paymentMethod!=null &&
              this.paymentMethod.equals(other.getPaymentMethod()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.errorCode==null && other.getErrorCode()==null) || 
             (this.errorCode!=null &&
              this.errorCode.equals(other.getErrorCode()))) &&
            ((this.errorMessage==null && other.getErrorMessage()==null) || 
             (this.errorMessage!=null &&
              this.errorMessage.equals(other.getErrorMessage()))) &&
            ((this.metaData==null && other.getMetaData()==null) || 
             (this.metaData!=null &&
              this.metaData.equals(other.getMetaData()))) &&
            ((this.senderName==null && other.getSenderName()==null) || 
             (this.senderName!=null &&
              this.senderName.equals(other.getSenderName()))) &&
            ((this.callerName==null && other.getCallerName()==null) || 
             (this.callerName!=null &&
              this.callerName.equals(other.getCallerName()))) &&
            ((this.recipientName==null && other.getRecipientName()==null) || 
             (this.recipientName!=null &&
              this.recipientName.equals(other.getRecipientName()))) &&
            ((this.transactionParts==null && other.getTransactionParts()==null) || 
             (this.transactionParts!=null &&
              java.util.Arrays.equals(this.transactionParts, other.getTransactionParts()))) &&
            ((this.relatedTransactions==null && other.getRelatedTransactions()==null) || 
             (this.relatedTransactions!=null &&
              java.util.Arrays.equals(this.relatedTransactions, other.getRelatedTransactions()))) &&
            ((this.statusHistory==null && other.getStatusHistory()==null) || 
             (this.statusHistory!=null &&
              java.util.Arrays.equals(this.statusHistory, other.getStatusHistory())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getTransactionId() != null) {
            _hashCode += getTransactionId().hashCode();
        }
        if (getCallerTransactionDate() != null) {
            _hashCode += getCallerTransactionDate().hashCode();
        }
        if (getDateReceived() != null) {
            _hashCode += getDateReceived().hashCode();
        }
        if (getDateCompleted() != null) {
            _hashCode += getDateCompleted().hashCode();
        }
        if (getTransactionAmount() != null) {
            _hashCode += getTransactionAmount().hashCode();
        }
        if (getFees() != null) {
            _hashCode += getFees().hashCode();
        }
        if (getCallerTokenId() != null) {
            _hashCode += getCallerTokenId().hashCode();
        }
        if (getSenderTokenId() != null) {
            _hashCode += getSenderTokenId().hashCode();
        }
        if (getRecipientTokenId() != null) {
            _hashCode += getRecipientTokenId().hashCode();
        }
        if (getPrepaidInstrumentId() != null) {
            _hashCode += getPrepaidInstrumentId().hashCode();
        }
        if (getCreditInstrumentId() != null) {
            _hashCode += getCreditInstrumentId().hashCode();
        }
        if (getOperation() != null) {
            _hashCode += getOperation().hashCode();
        }
        if (getPaymentMethod() != null) {
            _hashCode += getPaymentMethod().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getErrorCode() != null) {
            _hashCode += getErrorCode().hashCode();
        }
        if (getErrorMessage() != null) {
            _hashCode += getErrorMessage().hashCode();
        }
        if (getMetaData() != null) {
            _hashCode += getMetaData().hashCode();
        }
        if (getSenderName() != null) {
            _hashCode += getSenderName().hashCode();
        }
        if (getCallerName() != null) {
            _hashCode += getCallerName().hashCode();
        }
        if (getRecipientName() != null) {
            _hashCode += getRecipientName().hashCode();
        }
        if (getTransactionParts() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTransactionParts());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTransactionParts(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getRelatedTransactions() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRelatedTransactions());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRelatedTransactions(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getStatusHistory() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getStatusHistory());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getStatusHistory(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TransactionDetail.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TransactionDetail"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TransactionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("callerTransactionDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CallerTransactionDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateReceived");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DateReceived"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateCompleted");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DateCompleted"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TransactionAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "Amount"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fees");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Fees"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "Amount"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("callerTokenId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CallerTokenId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("senderTokenId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SenderTokenId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recipientTokenId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RecipientTokenId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("prepaidInstrumentId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PrepaidInstrumentId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creditInstrumentId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CreditInstrumentId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("operation");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Operation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "FPSOperation"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paymentMethod");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PaymentMethod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "PaymentMethod"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TransactionStatus"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errorCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ErrorCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errorMessage");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ErrorMessage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("metaData");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MetaData"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("senderName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SenderName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("callerName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CallerName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recipientName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RecipientName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionParts");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TransactionParts"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TransactionPart"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("relatedTransactions");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RelatedTransactions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "RelatedTransaction"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statusHistory");
        elemField.setXmlName(new javax.xml.namespace.QName("", "StatusHistory"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "StatusChange"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
