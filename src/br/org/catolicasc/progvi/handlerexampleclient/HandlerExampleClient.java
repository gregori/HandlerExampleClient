/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.catolicasc.progvi.handlerexampleclient;

import br.org.catolicasc.progvi.handlerexampleclient.handler.HeaderHandlerResolver;
import br.org.catolicasc.progvi.serverinfo.ServerInfo;
import br.org.catolicasc.progvi.serverinfo.ServerInfo_Service;
import br.org.catolicasc.progvi.serverinfo.UnknownHostException_Exception;

/**
 *
 * @author rodrigo
 */
public class HandlerExampleClient {

    /**
     * @param args the command line arguments
     * @throws br.org.catolicasc.progvi.serverinfo.UnknownHostException_Exception
     */
    public static void main(String[] args) throws UnknownHostException_Exception {
        ServerInfo_Service ss = new ServerInfo_Service();
        HeaderHandlerResolver handlerResolver = new HeaderHandlerResolver();
        ss.setHandlerResolver(handlerResolver);
        ServerInfo si = ss.getServerInfoPort();
        System.out.println(si.getServerName());
    }
    
}
