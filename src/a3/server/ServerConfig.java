package a3.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ServerConfig {

	private Properties props;
	
	public ServerConfig(String configName) {
		this.props = loadProperties(configName.replace('/', File.separatorChar));
	}
	
	 /**
     * Get <code>long</code> value of key property.
     * 
     * @param key Key to search for.
     * @return Value of key property.
     * @throws NumberFormatException If value is not a <code>long</code>.
     */
    public long getLong(final String key) throws NumberFormatException {
        return Long.parseLong(this.props.getProperty(key));
    }
    
    /**
     * Get <code>long</code> value of key property. If the property
     * doesn't exist, the <code>defaultValue</code> will be returned.
     * 
     * @param key Key to search for.
     * @param defaultValue The value returned if the property doesn't exist.
     * @return Value of key property.
     */
    public long getLong(final String key, final long defaultValue) {
        String val = this.props.getProperty(key);
        if (val != null) {
            try {
                return Long.parseLong(val);
            } catch (NumberFormatException e) {
                System.out.println("Value of '" + key + "' is not a long. "
                        + "Using specified default value '" + defaultValue + "'");
                e.printStackTrace();
            }
        }
        return defaultValue;
    }
    
    /**
     * Get <code>int</code> value of key property.
     * 
     * @param key Key to search for.
     * @return Value of key property.
     * @throws NumberFormatException If value is not an <code>int</code>.
     */
    public int getInt(final String key) throws NumberFormatException {
        return Integer.parseInt(this.props.getProperty(key));
    }
    
    /**
     * Get <code>int</code> value of key property. If the property
     * doesn't exist, the <code>defaultValue</code> will be returned.
     * 
     * @param key Key to search for.
     * @param defaultValue The value returned if the property doesn't exist.
     * @return Value of key property.
     */
    public int getInt(final String key, final int defaultValue) {
        String val = this.props.getProperty(key);
        if (val != null) {
            try {
                return Integer.parseInt(val);
            } catch (NumberFormatException e) {
                System.out.println("Value of '" + key + "' is not an integer. "
                        + "Using specified default value '" + defaultValue + "'");
                e.printStackTrace();
            }
        }
        return defaultValue;
    }
    
    /**
     * Get <code>boolean</code> value of key property.
     * 
     * @param key Key to search for.
     * @return Value of key property.
     */
    public boolean getBoolean(final String key) {
        return Boolean.parseBoolean(this.props.getProperty(key));
    }
    
    /**
     * Get <code>boolean</code> value of key property. If the property
     * doesn't exist, the <code>defaultValue</code> will be returned.
     * 
     * @param key Key to search for.
     * @param defaultValue The value returned if the property doesn't exist.
     * @return Value of key property.
     */
    public boolean getBoolean(final String key, final boolean defaultValue) {
        String val = this.props.getProperty(key);
        if (val != null) {
            return Boolean.parseBoolean(val);
        } else {
            return defaultValue;
        }
    }
    
    /**
     * Get <code>String</code> value of key property.
     * 
     * @param key Key to search for.
     * @return Value of key property.
     */
    public String getString(final String key) {
        return this.props.getProperty(key);
    }
    
    /**
     * Get <code>String</code> value of a key property. If the property
     * doesn't exist, the <code>defaultValue</code> will be returned.
     * 
     * @param key The key of the property to return.
     * @param defaultValue The value returned if the property doesn't exist.
     * @return The property value specified by the key.
     */
    public String getString(final String key, final String defaultValue) {
        final String val = getString(key);
        if (val != null) {
            return val;
        } else {
            return defaultValue;
        }
    }
	
	private Properties loadProperties(String configName) {
		final Properties props = new Properties();
		try (InputStream is = new FileInputStream(configName)) {
			props.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return props;
	}
	
}
