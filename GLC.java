/* Leonardo Kazuhiko Kawazoe & Leonardo Piccioni de Almeida */
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class GLC{
	static ArrayList<String> variaveis = new ArrayList<String>();
	static ArrayList<String> terminais = new ArrayList<String>();
	static ArrayList<Regra> regras = new ArrayList<Regra>();
	
	public static void main(String[] args){
		String path = args[1];
		try{
			obtemGLC(new File(path + "inp-glc.txt")); /* obtem a gramatica livre do contexto a partir do arquivo */
			obtemCadeias(new File(path + "inp-cadeias.txt")); /* obtem as cadeias a ser analisadas a partir do arquivo */
			/* CONTINUAR */
		}
		catch(FileNotFoundException e){
			System.out.println("Arquivo nao encontrado. Verifique se o diretorio foi corretamente especificado. Em caso de duvida, leia o arquivo README.txt.");
		}
	}

	static void obtemGLC(File f) throws FileNotFoundException{
		Scanner sc = new Scanner(f);
		sc.useDelimiter(" "); /* usar espaco em branco como delimitador de caracteres */
		int num_variaveis = sc.nextInt();
		int num_terminais = sc.nextInt();
		int num_regras = sc.nextInt();
		for(int i = 0; i < num_variaveis; i++) variaveis.add(sc.next());
		for(int i = 0; i < num_terminais; i++) terminais.add(sc.next());
		for(int i = 0; i < num_regras; i++){
			String esq = sc.next();
			sc.next(); /* pula o simbolo ">" */
			String dir = sc.next();
			if(!ehTerminal(dir)){
				dir += " " + sc.next();
			}
			regras.add(new Regra(esq, dir));
		}
		/* CONTINUAR: regras, etc. */
	}
	
	static void obtemCadeias(File f) throws FileNotFoundException{
		/* CONTINUAR */
		Scanner sc = new Scanner(f);	
	}
	
	static boolean ehTerminal(String dir){
		for(String s: terminais)
			if(dir.equals(s)) return true;
		return false;
	}
	
}

class Regra{
	
	public String esq; /* variavel do lado esquerdo da regra */
	public String dir; /* variaveis do lado direito da regra*/
	
	public Regra(String e, String d){
		this.esq = e;
		this.dir = e;
	}

}