package com.twilio.sdk.deleters.api.v2010.account.call;

import com.twilio.sdk.clients.TwilioRestClient;
import com.twilio.sdk.deleters.Deleter;
import com.twilio.sdk.exceptions.ApiConnectionException;
import com.twilio.sdk.exceptions.ApiException;
import com.twilio.sdk.http.HttpMethod;
import com.twilio.sdk.http.Request;
import com.twilio.sdk.http.Response;
import com.twilio.sdk.resources.RestException;
import com.twilio.sdk.resources.api.v2010.account.call.Recording;

public class RecordingDeleter extends Deleter<Recording> {
    private final String accountSid;
    private final String callSid;
    private final String sid;

    /**
     * Construct a new RecordingDeleter.
     * 
     * @param accountSid The account_sid
     * @param callSid The call_sid
     * @param sid The sid
     */
    public RecordingDeleter(final String accountSid, 
                            final String callSid, 
                            final String sid) {
        this.accountSid = accountSid;
        this.callSid = callSid;
        this.sid = sid;
    }

    /**
     * Make the request to the Twilio API to perform the delete.
     * 
     * @param client TwilioRestClient with which to make the request
     */
    @Override
    @SuppressWarnings("checkstyle:linelength")
    public boolean execute(final TwilioRestClient client) {
        Request request = new Request(
            HttpMethod.DELETE,
            TwilioRestClient.Domains.API,
            "/2010-04-01/Accounts/" + this.accountSid + "/Calls/" + this.callSid + "/Recordings/" + this.sid + ".json",
            client.getAccountSid()
        );
        
        Response response = client.request(request);
        
        if (response == null) {
            throw new ApiConnectionException("Recording delete failed: Unable to connect to server");
        } else if (response.getStatusCode() != TwilioRestClient.HTTP_STATUS_CODE_NO_CONTENT) {
            RestException restException = RestException.fromJson(response.getStream(), client.getObjectMapper());
            if (restException == null) {
                throw new ApiException("Server Error, no content");
            }
        
            throw new ApiException(
                restException.getMessage(),
                restException.getCode(),
                restException.getMoreInfo(),
                restException.getStatus(),
                null
            );
        }
        
        return true;
    }
}