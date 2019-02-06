package com.uniwallet.utilities;
/**
*#################################################################
*#
*# @Sofware :  UNIWALLET APP
*# @Category:  Utilities Class
*# @Module  :  Convertor Module   
*# @Version :  V1_0
*# @Author  :  Etio@
*# @Licence :  Copyright (c)  2016, NSANO. All Rights Reserved.
*#
*# @Description: This class implements the utility module convertor
*#                     
*#
*##################################################################
*/


/**
 * @dependencies 
 */
import java.awt.List;
import java.io.StringWriter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;




/**
 * Utility for converting ResultSets into some Output formats
 */
public class Convertor {
	
	/**
	 * ############################################################################################################
	 * 												CONVERTOR  CLASS
	 * ############################################################################################################
	 */
	
    /**
     * Convert a result set into a JSON Array
     *
     * @param resultSet
     * @return a JSONArray
     * @throws Exception
     */
    public static JSONArray convertResultSetIntoJSON(ResultSet resultSet) throws Exception {
        JSONArray jsonArray = new JSONArray();
        JSONObject obj = new JSONObject();
        while (resultSet.next()) {
            int total_rows = resultSet.getMetaData().getColumnCount();
            // JSONObject obj = new JSONObject();
            obj = new JSONObject();
            for (int i = 0; i < total_rows; i++) {
                String columnName = resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase();
                Object columnValue = resultSet.getObject(i + 1);
                // if value in DB is null, then we set it to default value
                if (columnValue == null) {
                    columnValue = "null";
                }
                /*
                 Next if block is a hack. In case when in db we have values like price and price1 there's a bug in jdbc - 
                 both this names are getting stored as price in ResulSet. Therefore when we store second column value,
                 we overwrite original value of price. To avoid that, i simply add 1 to be consistent with DB.
                 */
                if (obj.has(columnName)) {
                    columnName += "1";
                }

                obj.put(columnName, columnValue.toString());
            }
            jsonArray.put(obj);
        }

        return jsonArray;
    }

    /**
     *
     * @param bool
     * @return
     */
    public static int converBooleanIntoInt(boolean bool) {
        if (bool) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     *
     * @param bool
     * @return
     */
    public static int convertBooleanStringIntoInt(String bool) {
        if (bool.equals("false")) {
            return 0;
        } else if (bool.equals("true")) {
            return 1;
        } else {
            throw new IllegalArgumentException("wrong value is passed to the method. Value is " + bool);
        }
    }
    
    
    
    /**
    * 
    * @param bool
    * @return
    */
   public static String  convertListToString(List list , String delimiter ) {
       String res = "";
       delimiter = delimiter == null ? " " : delimiter; 
       int limit = list.getItemCount();
	   for( int i = 0 ; i < limit  ; i++)
	   {
		   res  = res + list.getItem(i).toString();
		   if( i != (limit -1) )
		   {
			   res = res + delimiter;
		   }
	   }
	   return res;
   }
   

    /**
     *
     * @param value
     * @param format
     * @param locale
     * @return
     */
    public static double getDoubleOutOfString(String value, String format, Locale locale) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat f = new DecimalFormat(format, otherSymbols);
        String formattedValue = f.format(Double.parseDouble(value));
        double number = Double.parseDouble(formattedValue);
        return Math.round(number * 100.0) / 100.0;
    }

    /**
     * This module convert XML format into a String Format
     *
     * @param xmlRequestStr
     * @return String
     * @throws TransformerException
     */
    public static String convertXMLtoString(Document xmlRequestStr) throws TransformerException {
        DOMSource domSource = new DOMSource(xmlRequestStr);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(domSource, result);
        return writer.toString();
    }

    /**
     * This module convert jsonObject to XML string
     *
     * @param jsonObject
     * @return
     */
    public static String convertJsonObjecttoXML(JSONObject jsonObject) {
        return org.json.XML.toString(jsonObject);
    }
    



	/**
	 * ############################################################################################################
	 * 												END CONVERTOR  CLASS
	 * ############################################################################################################
	 */
}
