/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.catolicasc.progvi.handlerexampleclient.handler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 *
 * @author rodrigo
 */
public class MacAddressInjectHandler implements SOAPHandler<SOAPMessageContext> {
    
    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        System.out.println("Client : handleMessage()......");
        Boolean isRequest = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        
        // se é uma requisição - true  para saída, false para entrada
        if (isRequest) {
            try {
                SOAPMessage soapMsg = context.getMessage();
                SOAPEnvelope soapEnv = soapMsg.getSOAPPart().getEnvelope();
                SOAPHeader soapHeader = soapEnv.getHeader();
                
                // se nao há header, adiciona um
                if (soapHeader == null)
                    soapHeader = soapEnv.addHeader();
                
                String mac = getMacAddress();
                
                // adiciona um soapheader, com o nome "mac address"
                QName qname = new QName("http://ws.catolicasc.org.br", "macAddress");
                SOAPHeaderElement soapHeaderElement = soapHeader.addHeaderElement(qname);
                
                soapHeaderElement.setActor(SOAPConstants.URI_SOAP_ACTOR_NEXT);
                soapHeaderElement.addTextNode(mac);
                soapMsg.saveChanges();
                
                // debug
                soapMsg.writeTo(System.out);
            } catch (IOException | SOAPException e) {
                System.err.println(e);
            } 
            
        }
        return true;
    }
    
    @Override
    public Set<QName> getHeaders() {
        System.out.println("Client : getHeaders()......");
        return Collections.EMPTY_SET;
    }
    
    @Override
    public boolean handleFault(SOAPMessageContext messageContext) {
        System.out.println("Client : handleFault()......");
        return true;
    }
    
    @Override
    public void close(MessageContext context) {
        System.out.println("Client : close()......");
    }
    
    // Retorna o mac address do cliente
    private String getMacAddress() {
        InetAddress ip;
        StringBuilder sb = new StringBuilder();
        try {
            ip = InetAddress.getLocalHost();
            System.out.println("Current IP Address: " + ip.getHostAddress());
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();
            System.out.print("Current MAC Address : ");
            for (int i = 0; i < mac.length; i++) 
                sb.append(String.format("%02X%s", mac[i], 
                        (i < mac.length -1) ? ":" : ""));
            System.out.println(sb.toString());
        } catch (UnknownHostException | SocketException e) {
            System.err.println(e);
        }
        return sb.toString();
    }
    
}
