package com.tax.verify.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class VD {
    private Metadata metadata;
    private Data data;
    private List<Message> messages = null;
    private String error;

    @JsonProperty
    private TaxDetailResult TaxDetailResult;

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public com.tax.verify.dto.TaxDetailResult getTaxDetailResult() {
        return TaxDetailResult;
    }

    public void setTaxDetailResult(com.tax.verify.dto.TaxDetailResult taxDetailResult) {
        TaxDetailResult = taxDetailResult;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
