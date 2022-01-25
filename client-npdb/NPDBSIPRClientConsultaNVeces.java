import java.io.*;
import java.net.Socket;

import com.beanit.asn1bean.ber.ReverseByteArrayOutputStream;
import com.beanit.asn1bean.ber.types.BerAny;
import com.beanit.asn1bean.ber.types.BerInteger;

import npdb.sipr.*;

public class NPDBSIPRClientConsultaNVeces {

    private int PORT; // = 11610;
    private String SERVER; // = "172.18.248.245";
    private String msisdn;
    private int times;
    
        public NPDBSIPRClientConsultaNVeces(String ip, int port, String numero, int veces){
                //constructor para operacion
                SERVER = ip;
                PORT = port;
                msisdn = numero;
                times = veces;
        }

    public static void main(String[] args) {

        NPDBSIPRClientConsultaNVeces cliente;

        if (args.length!=4) {
                System.out.println("Invalid parameters.");
                System.out.println("Usage:");
                System.out.println("          NPDBSIPRClientConsulta <ip> <port> <msisdn> <times>");
                System.exit(-1);
        }

        cliente = new NPDBSIPRClientConsultaNVeces(args[0], Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]));
        cliente.execNVeces();

    }

    public void execNVeces(){

        Socket socket; // Socket para la comunicacion cliente servidor        
        try {            
                // Iniciamos el socket.          
            socket = new Socket(SERVER, PORT);//abre socket                

            ConsultaPortabilidad operacionNpdb = new ConsultaPortabilidad();
            operacionNpdb.setNumTelefono(new NumTelefono(msisdn.getBytes("UTF-8")));
           
            ReverseByteArrayOutputStream ostmp = new ReverseByteArrayOutputStream(1000);
            operacionNpdb.encode(ostmp,true);
            operacionNpdb.code = ostmp.getArray();
            ostmp.reset();  
            
            InterfazSGIPSDP peticion = new InterfazSGIPSDP();
            
            peticion.setNumSecuencia(new BerInteger(1));
            peticion.setMsgType(new BerInteger(88));  // OperacionPortabilidad       

            peticion.setMsg(new BerAny(operacionNpdb.code)); 
            
            ReverseByteArrayOutputStream osBer = new ReverseByteArrayOutputStream(5000);
            
            peticion.encode(osBer,true);
            
            peticion.encode(ostmp,true);
            peticion.code = ostmp.getArray();
            ostmp.reset();
            
            DataOutputStream os = new DataOutputStream (new BufferedOutputStream(socket.getOutputStream()));                                 
            
            byte[] encabezadoLongitud = new byte[2];
            encabezadoLongitud[0] = (byte) (peticion.code.length >> 8);
            encabezadoLongitud[1] = (byte) (peticion.code.length & 0xFF);

            DataInputStream is = new DataInputStream (new BufferedInputStream(socket.getInputStream( )));

            //empezamos el bucle de N veces/times
            for (int i=0; i<times; i++) {

            os.write(encabezadoLongitud);
            
            os.write(osBer.getArray());
            
            os.flush();

            int longitudReq = ((encabezadoLongitud[0] & 0xFF) << 8) | (encabezadoLongitud[1] & 0xFF);
            System.out.println("Header (size): [" + (short)(encabezadoLongitud[0] & 0xFF) + "," + (short)(encabezadoLongitud[1] & 0xFF) + "] -> " + longitudReq + "d -> 0x" + Integer.toHexString(longitudReq));
            System.out.println("Request: " + peticion);
            System.out.println("Peticion NPDB: "+operacionNpdb);
            System.out.println();
            
            is = new DataInputStream (new BufferedInputStream(socket.getInputStream( )));

            RspConsultaPortabilidad respuestaNpdb = new RspConsultaPortabilidad();
            
            is.read(encabezadoLongitud);
            
            int longitudRes = ((encabezadoLongitud[0] & 0xFF) << 8) | (encabezadoLongitud[1] & 0xFF);
            System.out.println("Header (size): [" + (short)(encabezadoLongitud[0] & 0xFF) + "," + (short)(encabezadoLongitud[1] & 0xFF) + "] -> " + longitudRes + "d -> 0x" + Integer.toHexString(longitudRes));            
            
            InterfazSGIPSDP respuesta = new InterfazSGIPSDP();
            respuesta.decode(is,true);            
            
            System.out.println("Respuesta: " + respuesta); 
            
            respuesta.getMsg().encode(ostmp);
            InputStream istmp = new ByteArrayInputStream(ostmp.getArray());
            respuestaNpdb.decode(istmp,true);
            //*//istmp.close();
            //*//ostmp.close();
            
            System.out.println("Respuesta NPDB: "+ respuestaNpdb);

            //cerramos el bucle de N veces/times
            }

            // Cerramos los flujos/Stream de E/S hacia el OCS.
            os.close();            
            is.close();
            osBer.close();

            // Cerramos el socket.
            socket.close();            
                                                
       } catch (IOException ex) {        
         System.err.println("ERROR> " + ex.getMessage());   
       } catch (Exception e) {
           System.out.println(e);
       }

   } //exec

} //class
