import java.io.*;
import java.net.Socket;

//import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
//import org.openmuc.jasn1.ber.types.BerAny;
//import org.openmuc.jasn1.ber.types.BerInteger;

import com.beanit.asn1bean.ber.ReverseByteArrayOutputStream;
import com.beanit.asn1bean.ber.types.BerAny;
import com.beanit.asn1bean.ber.types.BerInteger;

import npdb.sipr.*;

public class NPDBSIPRClient {

	//private final static int PORT = 7610;
	private final static int PORT = 11610;
    	private final static String SERVER = "172.18.248.245";
    
	public static void main(String[] args) {		
		
        Socket socket; // Socket para la comunicacion cliente servidor        
        try {            
        	// Iniciamos el socket.          
            socket = new Socket(SERVER, PORT);//abre socket                

            // Creamos la estructura consultaTotalSaldo a partir de la clase generada
            // para insertarla en la estructura ASN1 de la peticion. 
            
            //ConsultaTotalSaldo consulta = new ConsultaTotalSaldo();
            //consulta.setMsisdn(new Msisdn(new String("690893416").getBytes("UTF-8")));

            OperacionPortabilidad operacionNpdb = new OperacionPortabilidad();
	    operacionNpdb.setTipoOperacion(new TipoOperacionPortabilidad(0)); //0-alta; 1-baja; 2-actualizacion
	    operacionNpdb.setNumTelefono(new NumTelefono(new String("659104541").getBytes("UTF-8")));
	    operacionNpdb.setOperadora(new Operadora(777)); // NRN
            
            
            // Ahora codificamos a mano esta clase consultaTotalSaldo porque parece que no
            // lo hace en la parte anterior y es necesario para poder asignar sus bytes[] 
            // (propiedad code) a la estructura ASN1 de la peticion/request (ademáde poder 
            // calcular su longitud) y poder ponerla como cabecera de la trama ASN1
            ReverseByteArrayOutputStream ostmp = new ReverseByteArrayOutputStream(1000);
            operacionNpdb.encode(ostmp,true);
            operacionNpdb.code = ostmp.getArray();
            ostmp.reset();  
            
            // Empezamos a crear la estructura ASN.1 de la REQUEST.
            // Recordatorio: todas las peticiones/respuesta en OCS tienen la misma estructura
            // definida en InterfazSGIPSDP y que se completa con 2 bytes con su longitud al inicio
            // de la trama ASN1
            
            /***************************************************            
             * 
             * InterfazSGIPSDP ::= SEQUENCE
			 * {
			 *     numSecuencia [1] IMPLICIT INTEGER,
			 *     msgType      [2] IMPLICIT INTEGER,
			 *     msg              ANY DEFINED BY msgType
			 * }
             * 
             ***************************************************/
            
            InterfazSGIPSDP peticion = new InterfazSGIPSDP();
            
            // Ponemos directamente el numSecuencia 1 ya que ejecutamos en un solo hilo y una sópetició            // OCS responderáon una estructura ASN1 con el mismo numSecuencia para que sepamos correlar la respuesta
            // en caso de que lanzaramos varias consultas por el mismo hilo (no serál caso).
            peticion.setNumSecuencia(new BerInteger(1));
            
            // Ponemos directamente el tipo definido en la gramatica ASN1            
            
            /***********************************************
             *  consultaTotalSaldo OBJECT-TYPE
			 *  SYNTAX ConsultaTotalSaldo
			 *  ACCESS read-write
			 *  STATUS mandatory
			 *  ::= 116
             ***********************************************/

            peticion.setMsgType(new BerInteger(86));  // OperacionPortabilidad       

            // Usamos la representación bytes (propiedad code) de la clase ConsultaTotalSaldo 
            // que generamos anteriormente para asignarla al msg de la peticióequest
            peticion.setMsg(new BerAny(operacionNpdb.code)); 
            
            // OutputStream tipo BER generado por la libreríJasn1 y que debemos usar
            // para codificar el flujo ASN1
            ReverseByteArrayOutputStream osBer = new ReverseByteArrayOutputStream(5000);
            
            // Codificamos en el flujo creado la peticion/request
            peticion.encode(osBer,true);
            
            // Ahora codificamos a mano la misma peticion/request para forzar a que su
            // propiedad code se inicialize y se pueda usar su longitud (propiedad length) 
            // Por alguna razólibrerí, no lo hace en la parte anterior y es necesario 
            // forzarlo para poder calcular luego su longitud y poder ponerla como cabecera
            // de la trama ASN1
            peticion.encode(ostmp,true);
            peticion.code = ostmp.getArray();
            ostmp.reset();
            
            // Creamos el flujo de salida a partir del socket donde escribiremos la trama ASN1 codificada
            DataOutputStream os = new DataOutputStream (new BufferedOutputStream(socket.getOutputStream()));                                 
            
            // Creamos la parte de cabecera (2 bytes con la longitud de la peticion/request)
            // que debe preceder siempre a todas las peticiones al OCS
            byte[] encabezadoLongitud = new byte[2];
            encabezadoLongitud[0] = (byte) (peticion.code.length >> 8);
            encabezadoLongitud[1] = (byte) (peticion.code.length & 0xFF);

            // Escribimos en el flujo de salida del socket la cabecera (es lo primero)
            os.write(encabezadoLongitud);
            
            // Escribimos en el flujo de salida del sockey los bytes de la petició partir
            // de los bytes del stream de salida tipo BER que ya estaba generado anteriormente.
            os.write(osBer.getArray());
            
            // Forzamos la salida del flujo de datos al socket hacia el OCS.
            os.flush();

            // Imprimimos peticion/request que hemos mandado con su cabecera (size)
            // Imprimimos tambiéla clase que mepea la peticion con sus campos.
            int longitudReq = ((encabezadoLongitud[0] & 0xFF) << 8) | (encabezadoLongitud[1] & 0xFF);
            System.out.println("Header (size): [" + (short)(encabezadoLongitud[0] & 0xFF) + "," + (short)(encabezadoLongitud[1] & 0xFF) + "] -> " + longitudReq + "d -> 0x" + Integer.toHexString(longitudReq));
            System.out.println("Request: " + peticion);
            System.out.println("Peticion NPDB: "+operacionNpdb);
            System.out.println();
            
            // Generamos el flujo para recepcionar la respuesta del OCS
            // NOTA: si el OCS no entiende la peticióo responde nada.
            DataInputStream is = new DataInputStream (new BufferedInputStream(socket.getInputStream( )));

            // Creamos la clase java generada por la libreríJasn1 que implementa la respuesta
            // del OCS. Esta respuesta vendráecubierta por la misma estructura ASN1 de la petició           // llamada InterfazSGIPSDP
            RspOperacionPortabilidad respuestaNpdb = new RspOperacionPortabilidad();
            
            // Leemos los dos primeros bytes de la respuesta que sabemos seráa longitud
            // de la respuesta ASN1 del OCS (mismo protocolo que para enviar peticion).
            is.read(encabezadoLongitud);
            
            // Imprimimos la cabecera/size de la respuesta
            int longitudRes = ((encabezadoLongitud[0] & 0xFF) << 8) | (encabezadoLongitud[1] & 0xFF);
            System.out.println("Header (size): [" + (short)(encabezadoLongitud[0] & 0xFF) + "," + (short)(encabezadoLongitud[1] & 0xFF) + "] -> " + longitudRes + "d -> 0x" + Integer.toHexString(longitudRes));            
            
            // Creamos objeto con la estructura ASN1 de la respuesta del OCS y lo decodificamos de la respuesta del OCS
            InterfazSGIPSDP respuesta = new InterfazSGIPSDP();
            respuesta.decode(is,true);            
            
            // Imprimimos estructura ASN1 respuesta del OCS
            System.out.println("Respuesta: " + respuesta); 
            
            // Ahora codificamos a mano la misma response para poder decodificar en la clase
            // correspondiente a rspConsultaTotalSaldo y poder tratarla/manejarla como queramos
            // con sus propiedades get/set (antes era el tipo genéco BerAny con su ASN1 en crudo)
            respuesta.getMsg().encode(ostmp);
            InputStream istmp = new ByteArrayInputStream(ostmp.getArray());
            respuestaNpdb.decode(istmp,true);
            istmp.close();
            ostmp.close();
            
            // Imprimimos el objeto rspConsulta decodificado ya
            System.out.println("Respuesta NPDB: "+ respuestaNpdb);

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

	}

}

