import java.net.InetAddress;

import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.NfsFileSystem;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.Swap;
import org.hyperic.sigar.SigarException;

import java.io.*;


public class InformacionEq {
	 private SigarProxy proxy;
	private Sigar sigar = new Sigar();
	String Nombre,IP,CPU;
	Long Disco;
	Long Ram;
	File f = new File("Datos.json");
	public void imprimirInfo() {
        OperatingSystem sys = OperatingSystem.getInstance();
        System.out.println("Descripcion del SO\t" + sys.getDescription());
        System.out.println("Nombre del SO\t\t" + sys.getName());
        System.out.println("Arquitectura del SO\t" + sys.getArch());
        Nombre=sys.getName();
        }
	 public void imprimirInfoCPU() {
	        sigar = new Sigar();
	        CpuInfo[] infos = null;
	        CpuPerc[] cpus = null;
	        try {
	            infos = sigar.getCpuInfoList();
	            cpus = sigar.getCpuPercList();
	        } catch (SigarException e) {
	            e.printStackTrace();
	        }
	        CpuInfo info = infos[0];
	        long tamanioCache = info.getCacheSize();
	        System.out.println("Fabricante:\t\t" + info.getVendor());
	        System.out.println("Modelo\t\t\t" + info.getModel());
	        System.out.println("Mhz\t\t\t" + info.getMhz());
	        System.out.println("Total CPUs\t\t" + info.getTotalCores());
	        CPU=Integer.toString(info.getTotalCores());
	        if ((info.getTotalCores() != info.getTotalSockets())
	                || (info.getCoresPerSocket() > info.getTotalCores())) {
	            System.out.println("CPUs fisiscas\t\t" + info.getTotalSockets());
	            System.out
	                    .println("Nucleos por CPU\t\t" + info.getCoresPerSocket());
	        }
	        
	 }
	 public void InfoSistemaArchivos() {
	        sigar = new Sigar();
	        proxy = SigarProxyCache.newInstance(sigar);
	    }
	 public void imprimirInfoSistemadeArchivos() throws SigarException {
	        FileSystem[] listaSistemaArchivos = proxy.getFileSystemList();
	        System.out.println("\ndispos.|total|usado|disponible|%uso|dir|tipo\n");
	        for (int i = 0; i < listaSistemaArchivos.length; i++)
	            imprimirSistemaArchivos(listaSistemaArchivos[i]);
	    }    public void imprimirSistemaArchivos(FileSystem sistemaArchivos)
	            throws SigarException {
	        long usado, disponible, total, porcentaje;
	        try {
	            FileSystemUsage uso;
	            if (sistemaArchivos instanceof NfsFileSystem) {
	                NfsFileSystem nfs = (NfsFileSystem) sistemaArchivos;
	                if (!nfs.ping()) {
	                    System.out.println(nfs.getUnreachableMessage());
	                    return;
	                }
	            } uso = sigar.getFileSystemUsage(sistemaArchivos.getDirName());
	            usado = uso.getTotal() - uso.getFree();
	            disponible = uso.getAvail();
	            total = uso.getTotal();
	            porcentaje = (long) (uso.getUsePercent() * 100);
	        } catch (SigarException e) {
	            // por ejemplo, si en al procesar D:\ en windows falla
	            // con "Device not ready"
	            usado = disponible = total = porcentaje = 0;
	        }
	        String porcentajeUso;
	        if (porcentaje == 0)
	            porcentajeUso = "-";
	        else
	            porcentajeUso = porcentaje + "%";
	        System.out.print(sistemaArchivos.getDevName());
	        System.out.print("|" + total);
	        System.out.print("|" + usado);
	        System.out.print("|" + disponible);
	        System.out.print("|" + porcentajeUso);
	        System.out.print("|" + sistemaArchivos.getDirName());
	        System.out.println("|" + sistemaArchivos.getSysTypeName());
	    }
	    public void imprimirInfoMemoria() throws SigarException {
			Long Ra,Dis;
	        Mem memoria = sigar.getMem();
	        Swap intercambio = sigar.getSwap();
	        System.out.println("Cantidad de memoria RAM: "+ memoria.getRam() + "MB");
	        System.out.println("Total: "+enBytes(memoria.getTotal()));
	        System.out.println("Usada: "+enBytes(memoria.getUsed()));
	        Ra=enBytes(memoria.getUsed());
	        Ram=Ra;
	        System.out.println("Disponible: "+enBytes(memoria.getFree()));
	        System.out.println("Memoria SWAP total: "+enBytes(intercambio.getTotal()));
	        System.out.println("Memoria SWAP usada: "+enBytes(intercambio.getUsed()));
	        System.out.println("Memoria SWAP libre: "+enBytes(intercambio.getFree()));
	        Dis=( intercambio.getFree());
	        Disco=Dis;
	     
	    }
	    private Long enBytes(long valor) {
	        return new Long(valor / 1024);
	        
	    }
	
	public void imprimirIP(){
		try 
		{
			String thisIp = InetAddress.getLocalHost().getHostAddress();
			System.out.println("IP:"+thisIp);
			IP=thisIp;
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	public void generar_informe(){//lo que debe ir en el archivo .json
		System.out.println(Nombre+",\n"+IP+",\n"+CPU+",\n"+Ram+",\n"+Disco);
	}
	public void EscribirArchivo(){
		try{
			FileWriter w = new FileWriter("C:/Users/AndresJara/Desktop/UNIVERSIDAD/sexto semestre/sistemas operativos/PROYECTO/Datos.json",false);
			BufferedWriter bw = new BufferedWriter(w);
			PrintWriter wr = new PrintWriter(bw);  
			wr.write("NOMBRE: "+Nombre+",\nIP:"+IP+"\n,"+"CPU:"+CPU+"\n,"+"RAM:"+Ram+",\nDISCO:"+Disco);//escribimos en el archivo
			wr.close();
			bw.close();
			System.out.print("archivo listo");
			}catch(IOException e){};
		 }

	
	public static void main(String [] arg) throws SigarException{
		InformacionEq obj=new InformacionEq();
		obj.imprimirInfo();
		obj.imprimirInfoCPU();
		obj.imprimirInfoMemoria();
		obj.imprimirIP();
		obj.generar_informe();
		obj.EscribirArchivo();
	}

}

