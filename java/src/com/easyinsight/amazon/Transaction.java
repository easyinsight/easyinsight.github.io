/**
 * Transaction.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class Transaction  implements java.io.Serializable {
    private java.lang.String transactionId;

    private java.util.Calendar callerTransactionDate;

    private java.util.Calendar dateReceived;

    private java.util.Calendar dateCompleted;

    private com.easyinsight.amazon.Amount transactionAmount;

    private com.easyinsight.amazon.FPSOperation operation;

    private com.easyinsight.amazon.TransactionStatus status;

    private java.lang.String errorMessage;

    private java.lang.String errorCode;

    private java.lang.String metaData;

    private java.lang.String originalTransactionId;

    private com.easyinsight.amazon.TransactionPart[] transactionParts;

    private com.easyinsight.amazon.PaymentMethod paymentMethod;

    private java.lang.String senderName;

    private java.lang.String callerName;

    private java.lang.String recipientName;

    private com.easyinsight.amazon.Amount fees;

    private com.easyinsight.amazon.Amount balance;

    private java.lang.String callerTokenId;

    private java.lang.String senderTokenId;

    private java.lang.String recipientTokenId;

    public Transaction() {
    }

    public Transaction(
           java.lang.String transactionId,
           java.util.Calendar callerTransactionDate,
           java.util.Calendar dateReceived,
           java.util.Calendar dateCompleted,
           com.easyinsight.amazon.Amount transactionAmount,
           com.easyinsight.amazon.FPSOperation operation,
           com.easyinsight.amazon.TransactionStatus status,
           java.lang.String errorMessage,
           java.lang.String errorCode,
           java.lang.String metaData,
           java.lang.String originalTransactionId,
           com.easyinsight.amazon.TransactionPart[] transactionParts,
           com.easyinsight.amazon.PaymentMethod paymentMethod,
           java.lang.String senderName,
           java.lang.String callerName,
           java.lang.String recipientName,
           com.easyinsight.amazon.Amount fees,
           com.easyinsight.amazon.Amount balance,
           java.lang.String callerTokenId,
           java.lang.String senderTokenId,
           java.lang.String recipientTokenId) {
           this.transactionId = transactionId;
           this.callerTransactionDate = callerTransactionDate;
           this.dateReceived = dateReceived;
           this.dateCompleted = dateCompleted;
           this.transactionAmount = transactionAmount;
           this.operation = operation;
           this.status = status;
           this.errorMessage = errorMessage;
           this.errorCode = errorCode;
           this.metaData = metaData;
           this.originalTransactionId = originalTransactionId;
           this.transactionParts = transactionParts;
           this.paymentMethod = paymentMethod;
           this.senderName = senderName;
           this.callerName = callerName;
           this.recipientName = recipientName;
           this.fees = fees;
           this.balance = balance;
           this.callerTokenId = callerTokenId;
           this.senderTokenId = senderTokenId;
           this.recipientTokenId = recipientTokenId;
    }


    /**
     * Gets the transactionId value for this Transaction.
     * 
     * @return transactionId
     */
    public java.lang.String getTransactionId() {
        return transactionId;
    }


    /**
     * Sets the transactionId value for this Transaction.
     * 
     * @param transactionId
     */
    public void setTransactionId(java.lang.String transactionId) {
        this.transactionId = transactionId;
    }


    /**
     * Gets the callerTransactionDate value for this Transaction.
     * 
     * @return callerTransactionDate
     */
    public java.util.Calendar getCallerTransactionDate() {
        return callerTransactionDate;
    }


    /**
     * Sets the callerTransactionDate value for this Transaction.
     * 
     * @param callerTransactionDate
     */
    public void setCallerTransactionDate(java.util.Calendar callerTransactionDate) {
        this.callerTransactionDate = callerTransactionDate;
    }


    /**
     * Gets the dateReceived value for this Transaction.
     * 
     * @return dateReceived
     */
    public java.util.Calendar getDateReceived() {
        return dateReceived;
    }


    /**
     * Sets the dateReceived value for this Transaction.
     * 
     * @param dateReceived
     */
    public void setDateReceived(java.util.Calendar dateReceived) {
        this.dateReceived = dateReceived;
    }


    /**
     * Gets the dateCompleted value for this Transaction.
     * 
     * @return dateCompleted
     */
    public java.util.Calendar getDateCompleted() {
        return dateCompleted;
    }


    /**
     * Sets the dateCompleted value for this Transaction.
     * 
     * @param dateCompleted
     */
    public void setDateCompleted(java.util.Calendar dateCompleted) {
        this.dateCompleted = dateCompleted;
    }


    /**
     * Gets the transactionAmount value for this Transaction.
     * 
     * @return transactionAmount
     */
    public com.easyinsight.amazon.Amount getTransactionAmount() {
        return transactionAmount;
    }


    /**
     * Sets the transactionAmount value for this Transaction.
     * 
     * @param transactionAmount
     */
    public void setTransactionAmount(com.easyinsight.amazon.Amount transactionAmount) {
        this.transactionAmount = transactionAmount;
    }


    /**
     * Gets the operation value for this Transaction.
     * 
     * @return operation
     */
    public com.easyinsight.amazon.FPSOperation getOperation() {
        return operation;
    }


    /**
     * Sets the operation value for this Transaction.
     * 
     * @param operation
     */
    public void setOperation(com.easyinsight.amazon.FPSOperation operation) {
        this.operation = operation;
    }


    /**
     * Gets the status value for this Transaction.
     * 
     * @return status
     */
    public com.easyinsight.amazon.TransactionStatus getStatus() {
        return status;
    }


    /**
     * Sets the status value for this Transaction.
     * 
     * @param status
     */
    public void setStatus(com.easyinsight.amazon.TransactionStatus status) {
        this.status = status;
    }


    /**
     * Gets the errorMessage value for this Transaction.
     * 
     * @return errorMessage
     */
    public java.lang.String getErrorMessage() {
        return errorMessage;
    }


    /**
     * Sets the errorMessage value for this Transaction.
     * 
     * @param errorMessage
     */
    public void setErrorMessage(java.lang.String errorMessage) {
        this.errorMessage = errorMessage;
    }


    /**
     * Gets the errorCode value for this Transaction.
     * 
     * @return errorCode
     */
    public java.lang.String getErrorCode() {
        return errorCode;
    }


    /**
     * Sets the errorCode value for this Transaction.
     * 
     * @param errorCode
     */
    public void setErrorCode(java.lang.String errorCode) {
        this.errorCode = errorCode;
    }


    /**
     * Gets the metaData value for this Transaction.
     * 
     * @return metaData
     */
    public java.lang.String getMetaData() {
        return metaData;
    }


    /**
     * Sets the metaData value for this Transaction.
     * 
     * @param metaData
     */
    public void setMetaData(java.lang.String metaData) {
        this.metaData = metaData;
    }


    /**
     * Gets the originalTransactionId value for this Transaction.
     * 
     * @return originalTransactionId
     */
    public java.lang.String getOriginalTransactionId() {
        return originalTransactionId;
    }


    /**
     * Sets the originalTransactionId value for this Transaction.
     * 
     * @param originalTransactionId
     */
    public void setOriginalTransactionId(java.lang.String originalTransactionId) {
        this.originalTransactionId = originalTransactionId;
    }


    /**
     * Gets the transactionParts value for this Transaction.
     * 
     * @return transactionParts
     */
    public com.easyinsight.amazon.TransactionPart[] getTransactionParts() {
        return transactionParts;
    }


    /**
     * Sets the transactionParts value for this Transaction.
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
     * Gets the paymentMethod value for this Transaction.
     * 
     * @return paymentMethod
     */
    public com.easyinsight.amazon.PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }


    /**
     * Sets the paymentMethod value for this Transaction.
     * 
     * @param paymentMethod
     */
    public void setPaymentMethod(com.easyinsight.amazon.PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


    /**
     * Gets the senderName value for this Transaction.
     * 
     * @return senderName
     */
    public java.lang.String getSenderName() {
        return senderName;
    }


    /**
     * Sets the senderName value for this Transaction.
     * 
     * @param senderName
     */
    public void setSenderName(java.lang.String senderName) {
        this.senderName = senderName;
    }


    /**
     * Gets the callerName value for this Transaction.
     * 
     * @return callerName
     */
    public java.lang.String getCallerName() {
        return callerName;
    }


    /**
     * Sets the callerName value for this Transaction.
     * 
     * @param callerName
     */
    public void setCallerName(java.lang.String callerName) {
        this.callerName = callerName;
    }


    /**
     * Gets the recipientName value for this Transaction.
     * 
     * @return recipientName
     */
    public java.lang.String getRecipientName() {
        return recipientName;
    }


    /**
     * Sets the recipientName value for this Transaction.
     * 
     * @param recipientName
     */
    public void setRecipientName(java.lang.String recipientName) {
        this.recipientName = recipientName;
    }


    /**
     * Gets the fees value for this Transaction.
     * 
     * @return fees
     */
    public com.easyinsight.amazon.Amount getFees() {
        return fees;
    }


    /**
     * Sets the fees value for this Transaction.
     * 
     * @param fees
     */
    public void setFees(com.easyinsight.amazon.Amount fees) {
        this.fees = fees;
    }


    /**
     * Gets the balance value for this Transaction.
     * 
     * @return balance
     */
    public com.easyinsight.amazon.Amount getBalance() {
        return balance;
    }


    /**
     * Sets the balance value for this Transaction.
     * 
     * @param balance
     */
    public void setBalance(com.easyinsight.amazon.Amount balance) {
        this.balance = balance;
    }


    /**
     * Gets the callerTokenId value for this Transaction.
     * 
     * @return callerTokenId
     */
    public java.lang.String getCallerTokenId() {
        return callerTokenId;
    }


    /**
     * Sets the callerTokenId value for this Transaction.
     * 
     * @param callerTokenId
     */
    public void setCallerTokenId(java.lang.String callerTokenId) {
        this.callerTokenId = callerTokenId;
    }


    /**
     * Gets the senderTokenId value for this Transaction.
     * 
     * @return senderTokenId
     */
    public java.lang.String getSenderTokenId() {
        return senderTokenId;
    }


    /**
     * Sets the senderTokenId value for this Transaction.
     * 
     * @param senderTokenId
     */
    public void setSenderTokenId(java.lang.String senderTokenId) {
        this.senderTokenId = senderTokenId;
    }


    /**
     * Gets the recipientTokenId value for this Transaction.
     * 
     * @return recipientTokenId
     */
    public java.lang.String getRecipientTokenId() {
        return recipientTokenId;
    }


    /**
     * Sets the recipientTokenId value for this Transaction.
     * 
     * @param recipientTokenId
     */
    public void setRecipientTokenId(java.lang.String recipientTokenId) {
        this.recipientTokenId = recipientTokenId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Transaction)) return false;
        Transaction other = (Transaction) obj;
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
            ((this.operation==null && other.getOperation()==null) || 
             (this.operation!=null &&
              this.operation.equals(other.getOperation()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.errorMessage==null && other.getErrorMessage()==null) || 
             (this.errorMessage!=null &&
              this.errorMessage.equals(other.getErrorMessage()))) &&
            ((this.errorCode==null && other.getErrorCode()==null) || 
             (this.errorCode!=null &&
              this.errorCode.equals(other.getErrorCode()))) &&
            ((this.metaData==null && other.getMetaData()==null) || 
             (this.metaData!=null &&
              this.metaData.equals(other.getMetaData()))) &&
            ((this.originalTransactionId==null && other.getOriginalTransactionId()==null) || 
             (this.originalTransactionId!=null &&
              this.originalTransactionId.equals(other.getOriginalTransactionId()))) &&
            ((this.transactionParts==null && other.getTransactionParts()==null) || 
             (this.transactionParts!=null &&
              java.util.Arrays.equals(this.transactionParts, other.getTransactionParts()))) &&
            ((this.paymentMethod==null && other.getPaymentMethod()==null) || 
             (this.paymentMethod!=null &&
              this.paymentMethod.equals(other.getPaymentMethod()))) &&
            ((this.senderName==null && other.getSenderName()==null) || 
             (this.senderName!=null &&
              this.senderName.equals(other.getSenderName()))) &&
            ((this.callerName==null && other.getCallerName()==null) || 
             (this.callerName!=null &&
              this.callerName.equals(other.getCallerName()))) &&
            ((this.recipientName==null && other.getRecipientName()==null) || 
             (this.recipientName!=null &&
              this.recipientName.equals(other.getRecipientName()))) &&
            ((this.fees==null && other.getFees()==null) || 
             (this.fees!=null &&
              this.fees.equals(other.getFees()))) &&
            ((this.balance==null && other.getBalance()==null) || 
             (this.balance!=null &&
              this.balance.equals(other.getBalance()))) &&
            ((this.callerTokenId==null && other.getCallerTokenId()==null) || 
             (this.callerTokenId!=null &&
              this.callerTokenId.equals(other.getCallerTokenId()))) &&
            ((this.senderTokenId==null && other.getSenderTokenId()==null) || 
             (this.senderTokenId!=null &&
              this.senderTokenId.equals(other.getSenderTokenId()))) &&
            ((this.recipientTokenId==null && other.getRecipientTokenId()==null) || 
             (this.recipientTokenId!=null &&
              this.recipientTokenId.equals(other.getRecipientTokenId())));
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
        if (getOperation() != null) {
            _hashCode += getOperation().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getErrorMessage() != null) {
            _hashCode += getErrorMessage().hashCode();
        }
        if (getErrorCode() != null) {
            _hashCode += getErrorCode().hashCode();
        }
        if (getMetaData() != null) {
            _hashCode += getMetaData().hashCode();
        }
        if (getOriginalTransactionId() != null) {
            _hashCode += getOriginalTransactionId().hashCode();
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
        if (getPaymentMethod() != null) {
            _hashCode += getPaymentMethod().hashCode();
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
        if (getFees() != null) {
            _hashCode += getFees().hashCode();
        }
        if (getBalance() != null) {
            _hashCode += getBalance().hashCode();
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
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Transaction.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "Transaction"));
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
        elemField.setFieldName("operation");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Operation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "FPSOperation"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "TransactionStatus"));
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
        elemField.setFieldName("errorCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ErrorCode"));
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
        elemField.setFieldName("originalTransactionId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "OriginalTransactionId"));
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
        elemField.setFieldName("paymentMethod");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PaymentMethod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "PaymentMethod"));
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
        elemField.setFieldName("fees");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Fees"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "Amount"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("balance");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Balance"));
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
