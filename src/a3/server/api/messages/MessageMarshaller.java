package a3.server.api.messages;

import java.io.IOException;
import java.util.function.Function;

import a3.server.api.messages.impl.CreateMessage;

public enum MessageMarshaller {

	INSTANCE;
	
	public String marshal(Message message) {
		return message.toMessageString();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T unmarshal(String message) throws IOException {
		if (null == message || message.trim().equals("")) {
            throw new IOException("Message is null or empty!");
        }
		
        try {
            
        	final Function<String, String> parse = 
        			new unmarshalerFactory<String>().curryunmarshaler
        				.apply((Class<String>) MessageType.getMessageType(message).getUnmarshalClass());
        	return (T) parse.apply(message);
        } catch (RuntimeException e) {
            throw new IOException(e.getMessage(), e);
        }
	}
	
	private class unmarshalerFactory<T> {
        public Function<Class<T>, Function<String, T>> curryunmarshaler = unmarshalClass -> message -> {
            try {
                return unmarshal(unmarshalClass, message);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        };
    }
	
	public <T> T unmarshal(Class<T> unmarshalClass, String message) throws IOException {
        if (null == message || message.trim().equals("")) {
            throw new IOException("Message '" + message + "' is invalid!");
        }
        if (unmarshalClass == null) {
            throw new NullPointerException("unmarshal Class cannot be null!");
        }
        
        final String canonicalName = unmarshalClass.getCanonicalName();
        Object obj = null;
        
        // find matching unmarshal class and then do it...
        if (canonicalName.contentEquals(CreateMessage.class.getCanonicalName())) {
        	obj = new CreateMessage().fromMessageString(message);
        } else if (canonicalName.contentEquals(null)) {
        	// etc..
        }
        
        if (obj == null) {
            throw new IOException("Failed to unmarshal class");
        }
        if (!unmarshalClass.isInstance(obj)) {
            throw new IOException("Incompatible unmarshal class");
        }
        
        return unmarshalClass.cast(obj);
    }
	
}
