/* Leonardo Kazuhiko Kawazoe & Leonardo Piccioni de Almeida */
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class GLC{
	static final String VAZIO = "&";
	static String INICIAL;
	static ArrayList<String> variaveis = new ArrayList<String>();
	static ArrayList<String> terminais = new ArrayList<String>();
	static ArrayList<Regra> regras = new ArrayList<Regra>();
	static ArrayList<String> cadeias =  new ArrayList<String>();
		
	public static void main(String[] args){
		try{
			obtemGLC(new File("inp-glc.txt")); /* obtem a gramatica livre do contexto a partir do arquivo */
			obtemCadeias(new File("inp-cadeias.txt")); /* obtem as cadeias a ser analisadas a partir do arquivo */
			
			/* referenciacao aos arquivos de saida */
			String diretorioAtual = System.getProperty("user.dir");
			File out_status = new File(diretorioAtual + "/out-status.txt");
			if(!out_status.exists()) out_status.createNewFile();
			BufferedWriter pw_status = new BufferedWriter(new FileWriter(out_status.getPath(), true));
			File out_tabela = new File(diretorioAtual + "/out-tabela.txt");
			if(!out_tabela.exists()) out_tabela.createNewFile();
			BufferedWriter pw_tabela = new BufferedWriter(new FileWriter(out_tabela.getPath(), true));
			pw_tabela.write(cadeias.size() + "\n");
						
			for(String cadeia : cadeias){
				if(cadeia.equals(VAZIO)){
					/* tratamento de cadeia vazia */
					Lista l = new Lista();
					for(Regra regra : regras)
						if(regra.dir.equals(VAZIO)) l.add(regra.esq);
					if(l.inicio != null) pw_status.write("1");
					else pw_status.write("0");
					if(cadeias.get(cadeias.size() - 1) != cadeia) pw_status.write(" ");
					pw_tabela.write(cadeia + "\n");
				}
				else{
					int max = Math.round(cadeia.length() / 2) + 1; /* tamanho do lado da matriz quadrada */
					Lista[][] tabela = new Lista[max][max]; /* tabela contendo listas ligadas para o valor de cada indice*/
					/* obtendo a diagonal exterior */
					for(int i = 0; i < max; i++){
						tabela[i][i] = new Lista();
						String terminal = String.valueOf(cadeia.charAt(2 * i)); /* caracteres sao divididos por espacos */
						for(Regra regra : regras)
							if(regra.dir.equals(terminal)) tabela[i][i].add(regra.esq);
					}
					/* entao o resto da tabela */
					for(int d = 1; d < max; d++) /* diagonal */
						for(int i = 0, j = d; j < max; i++, j++){
							tabela[i][j] = new Lista();
							for(int ix = i + 1, jx = i; jx < j; jx++, ix++)
								for(Regra regra : regras)
									if(tabela[i][jx] != null && tabela[ix][j] != null)
										for(No n1 = tabela[i][jx].inicio; n1 != null; n1 = n1.prox)
											for(No n2 = tabela[ix][j].inicio; n2 != null; n2 = n2.prox)
												if(regra.dir.equals(n1.valor + " " + n2.valor)) tabela[i][j].add(regra.esq);
						}
					/* gerando o log */
					if(tabela[0][max - 1].contem(INICIAL)) pw_status.write("1");
					else pw_status.write("0");
					if(cadeias.get(cadeias.size() - 1) != cadeia) pw_status.write(" ");
					pw_tabela.write(cadeia + "\n");
					for(int i = 0; i < max; i++)
						for(int j = i; j < max; j++){
							pw_tabela.write((i + 1) + " " + ((int)j + 1));
							if(tabela[i][j] != null)
								for(No n = tabela[i][j].inicio; n != null; n = n.prox)
									pw_tabela.write(" " + n.valor);
							pw_tabela.write("\n");
						}
				}
			}
			pw_status.close();
			pw_tabela.close();
		}
		catch(FileNotFoundException e){
			System.out.println("Arquivo nao encontrado.");
		}
		catch(IOException e){
			System.out.println("Nao foi possivel escrever/sobrescrever o arquivo.");
		}
	}

	static void obtemGLC(File f) throws FileNotFoundException{
		Scanner sc = new Scanner(f);
		int num_variaveis = sc.nextInt();
		int num_terminais = sc.nextInt();
		int num_regras = sc.nextInt();
		for(int i = 0; i < num_variaveis; i++) variaveis.add(sc.next());
		INICIAL = variaveis.get(0);
		for(int i = 0; i < num_terminais; i++) terminais.add(sc.next());
		for(int i = 0; i < num_regras; i++)
			while(sc.hasNext()){
			String esq = sc.next();
			sc.useDelimiter(" "); /* utiliza espaco em branco como delimitador de caracteres */
			sc.next(); /* pula o simbolo ">" */
			sc.reset(); /* remove delimitador de caracteres */
			String dir = sc.next();
			if(!ehTerminal(dir) && !dir.equals(VAZIO))
				dir += " " + sc.next();
			regras.add(new Regra(esq, dir));
			}
	}
	
	static void obtemCadeias(File f) throws FileNotFoundException{
		Scanner sc = new Scanner(f);
		int num_cadeias = sc.nextInt();
		sc.nextLine(); /* pula para a linha da primeira cadeia */
		for(int i = 0; i < num_cadeias; i++) cadeias.add(sc.nextLine());	
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
		this.dir = d;
	}
}

class Lista{
	No inicio = null;
	
	public void add(String s){
		if(inicio == null)
			inicio = new No(s);
		else if(!contem(s)){
			No aux = inicio;
			while(aux.prox != null)
				aux = aux.prox;
			aux.prox = new No(s);
		}
	}
	
	boolean contem(String s){
		No aux = inicio;
		if(aux == null) return false;
		do{
			if(aux.valor.equals(s)) return true;
			aux = aux.prox;
		} while(aux != null);
		return false;
	}
	
	public Lista(){
		inicio = null;
	}
}

class No{
	String valor;
	No prox;
	
	public No(String s){
		valor = s;
		prox = null;
	}
}