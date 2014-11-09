/* Leonardo Kazuhiko Kawazoe & Leonardo Piccioni de Almeida */
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class GLC{
	static final String VAZIO = "&";
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
			PrintWriter pw_status = new PrintWriter(new BufferedWriter(new FileWriter(out_status.getPath(), true)));
			File out_tabela = new File(diretorioAtual + "/out-tabela.txt");
			if(!out_tabela.exists()) out_tabela.createNewFile();
			PrintWriter pw_tabela = new PrintWriter(new BufferedWriter(new FileWriter(out_tabela.getPath(), true)));
			pw_tabela.println(cadeias.size());
			pw_tabela.close();
						
			for(String cadeia : cadeias){
				if(cadeia.equals(VAZIO)){
					/* tratamento de cadeia vazia */
					Lista l = new Lista();
					for(Regra regra : regras)
						if(regra.dir.equals(VAZIO)) l.add(regra.esq);
					if(l.inicio != null) pw_status.print("1");
					else pw_status.print("0");
					if(cadeias.get(cadeias.size() - 1) != cadeia) pw_status.print(" ");
					pw_status.close();
					pw_tabela.println(cadeia);
					pw_tabela.close();
				}
				else{
					int max = cadeia.length(); /* tamanho do lado da matriz quadrada */
					Lista[][] tabela = new Lista[max][max]; /* tabela contendo listas ligadas para o valor de cada indice*/
					/* obtendo a diagonal exterior */
					for(int i = 0; i < max; i++){
						String terminal = String.valueOf(cadeia.charAt(2 * i)); /* caracteres sao divididos por espacos */
						for(Regra regra : regras)
							if(regra.dir.equals(terminal)) tabela[i][i].add(regra.esq);
					}
					/* entao o resto da tabela */
					for(int d = 1; d < max; d++) /* diagonal */
						for(int j = 0, i = j + d; i < max; i++, j++)
							for(int ix = i + 1, jx = d; jx < j; jx++, ix++)
								for(Regra regra : regras)
									for(No n1 = tabela[i][jx].inicio; n1 != null; n1 = n1.prox)
										for(No n2 = tabela[ix][j].inicio; n2 != null; n2 = n2.prox)
											if(regra.dir.equals(n1 + " " + n2)) tabela[i][j].add(regra.esq);
					/* gerando o log */
					if(tabela[0][max - 1] != null) pw_status.print("1");
					else pw_status.print("0");
					if(cadeias.get(cadeias.size() - 1) != cadeia) pw_status.print(" ");
					pw_status.close();
					pw_tabela.println(cadeia);
					for(int i = 0; i < max; i++)
						for(int j = i; j < max; j++){
							pw_tabela.print(i + 1 + " " + j + 1);
							if(tabela[i][j] != null)
								for(No n = tabela[i][j].inicio; n != null; n = n.prox)
									pw_tabela.print(" " + n.valor);
							pw_tabela.print("\n");
						}
					pw_tabela.close();
				}
			}
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
		for(int i = 0; i < num_terminais; i++) terminais.add(sc.next());
		for(int i = 0; i < num_regras; i++){
			while(sc.hasNext()){
			String esq = sc.next();
			sc.useDelimiter(" "); /* utiliza espaco em branco como delimitador de caracteres */
			sc.next(); /* pula o simbolo ">" */
			sc.reset(); /* remove delimitador de caracteres */
			String dir = sc.next();
			if(!ehTerminal(dir))
				dir += " " + sc.next();
			regras.add(new Regra(esq, dir));
			}
		}
	}
	
	static void obtemCadeias(File f) throws FileNotFoundException{
		Scanner sc = new Scanner(f);
		int num_cadeias = sc.nextInt();
		sc.nextLine(); /* pula para a linha da primeira cadeia */
		for(int i = 0; i < num_cadeias; i++) cadeias.add(sc.nextLine());
		for(String cadeia : cadeias) System.out.println(cadeia);	
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

class Lista{
	No inicio = null;
	
	public void add(String s){
		if(inicio == null)
			inicio = new No(s);
		else if(!contem(s)){
			No aux = inicio;
			while(aux.prox != null) aux = aux.prox;
			aux.prox = new No(s);
		}
	}
	
	boolean contem(String s){
		No aux = inicio;
		if(aux == null) return false;
		do{
			if(aux.valor == s) return true;
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