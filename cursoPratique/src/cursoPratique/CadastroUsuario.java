package cursoPratique;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

public class CadastroUsuario {
    private String nome;
    private String email;
    private String senha;
    private boolean logado = false; //  controla o status de login
    
    // Mapa para armazenar os usuários cadastrados (utilizaremos o email como chave)
    private static Map<String, CadastroUsuario> usuariosCadastrados = new HashMap<>();
    private static final String ARQUIVO_USUARIOS = "usuarios.txt"; //arquivo dos usuarios
    
    //arrays para os eventos
    private List<Evento> eventos = new ArrayList<>();

    // Método para cadastrar um novo usuário
    public void cadastrarUsuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        usuariosCadastrados.put(email, this);
        salvarUsuarios();
        System.out.println("Usuário cadastrado com sucesso!");
    }

    // Método para realizar o login
    public boolean realizarLogin(String email, String senha) {
        if (usuariosCadastrados.containsKey(email)) {
            CadastroUsuario usuario = usuariosCadastrados.get(email);
            if (usuario.senha.equals(senha)) {// se a senha for igual a que esta cadastrada
                logado = true;// Atualizando o status de login para true
                this.nome = usuario.nome; // Define os detalhes do usuário atual
                this.email = usuario.email;
                this.senha = usuario.senha;
                return true;//faça o login
            } else {
                System.out.println("Senha incorreta!");// se não da senha incorreta
            }
        } else {
            System.out.println("Usuário não cadastrado!");
        }
        return false;
    }

    // Método para exibir os detalhes do usuário cadastrado
    public void exibirDetalhesUsuario() {
    	if (logado) {
            System.out.println("Detalhes do Usuário:");
            System.out.println("Nome: " + nome);
            System.out.println("Email: " + email);
            // não mostra a senha por questão de segurança
        } else {
            System.out.println("Você não está logado.");
        }
    }
    
    private static void salvarUsuarios() {
    	try(PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_USUARIOS))){//cria um novo arquivo de texto para armazenar os usuarios
    		for (CadastroUsuario usuario : usuariosCadastrados.values()) {
    			writer.println(usuario.nome + ", " + usuario.email + ", " + usuario.senha);
    		}
    	}catch (IOException e) {
    		System.out.println("Erro ao salvar usuários: " + e.getMessage());
    	}
    }
    // esse método ele lê o arquivo dos usuarios e ve se o usuario que vc digitou consta neste arquivo
    private static void carregarUsuario() {
    	try (Scanner scanner = new Scanner(new File(ARQUIVO_USUARIOS))){
    		while(scanner.hasNextLine()) {
    			String[] dadosUsuario = scanner.nextLine().split(", ");
    			if(dadosUsuario.length == 3) {
    				CadastroUsuario usuario = new CadastroUsuario();
    				usuario.nome = dadosUsuario[0];
                    usuario.email = dadosUsuario[1];
                    usuario.senha = dadosUsuario[2];
                    usuariosCadastrados.put(usuario.email, usuario);
    			}
    		}
    	} catch (FileNotFoundException e) {
    		
    	}
    }
      // aqui é o método de exibir o menu de eventos
    public void exibirMenuEventos(Scanner scanner) {
    	int opcao;
    	do {
    		System.out.println("\nMenu de Eventos:");
    		System.out.println("1. Adicionar evento");
    		System.out.println("2. Listar eventos");
    		System.out.println("3. Cadastrar presença em um evento");
    		System.out.println("4. Listar Meus Eventos");
    		System.out.println("5. Cancelar presença de um evento");
    		System.out.println("0. Voltar ao Menu");
    		System.out.print("Escolha uma opção: ");
    		opcao = scanner.nextInt();
    		scanner.nextLine();
    		
    		switch (opcao) {
    		case 1:
    			adicionarEvento(scanner);// se digitarmos 1 ele vai pro metodo de adiciona evento
    			break;
    		case 2:
    			listarEventos();// se digitar 2 vai pro metodo de listar os eventos
    			System.out.println("--------- ");
    			verificarEventoAtual();
    			System.out.println("--------- ");
    			
    			break;
    		case 3:
    			presencaEvento(scanner);
    			break;
    		case 4:
    			listarMeusEventos();
    			break;
    		case 5:
    			cancelarPresenca(scanner);
    			break;
    		case 0:
    			System.out.println("Voltando ao Menu Principal..."); // se for 0 volta pro menu
    			menu(scanner);
    			
                break;
            default:
                System.out.println("Opção inválida. Tente novamente.");// qualquer outro numero da opção invalida
    		}
    		
    	}while (opcao !=0);// faça esse loop enquanto não digitar 0
    }
    
    
    
    //método para adicionar os eventos
    private void adicionarEvento(Scanner scanner) {
        System.out.println("\nAdicionar evento:");
        System.out.print("Data e hora (yyyy-MM-dd HH:mm): ");
        String dataHoraStr = scanner.nextLine();
        try {
            LocalDateTime dataHora = LocalDateTime.parse(dataHoraStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            System.out.print("Endereço: ");
            String endereco = scanner.nextLine();
            System.out.print("Tipo de evento: ");
            String tipo = scanner.nextLine();
            System.out.print("Descrição: ");
            String descricao = scanner.nextLine();
            Evento evento = new Evento(dataHora, endereco, tipo,descricao);
            eventos.add(evento);
            salvarEventos();
            System.out.println("Evento adicionado com sucesso!");
        } catch (DateTimeParseException e) {
            System.out.println("Erro ao adicionar evento: Formato de data e hora inválido. Use o formato yyyy-MM-dd HH:mm.");
        }
    }
    //metodo para listar os eventos
    private void listarEventos() {
        System.out.println("\nLista de Eventos:");
        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento cadastrado");
        } else {
        	Map <String, List<Evento>> eventosPorTipo = new HashMap<>();
        	
        	for (Evento evento : eventos) {// aqui ele cria uma sepração dos eventos pelo tipo que colocamos no cadastro do evento
        		String tipo = evento.getTipo();
        		if(!eventosPorTipo.containsKey(tipo)) {
        			eventosPorTipo.put(tipo, new ArrayList<>());
        			
        		}
        		eventosPorTipo.get(tipo).add(evento);
        	}
        	
        	
        	
        	
            for (String tipo : eventosPorTipo.keySet()) {
            	List<Evento> eventosDoTipo = eventosPorTipo.get(tipo);// pega os tipos do eventos
            	System.out.println("Eventos do tipo " + tipo+":");// mostra o tipo
            	for (int i = 0; i < eventosDoTipo.size(); i++) {   //loop para ler os eventos             
                    Evento evento = eventosDoTipo.get(i);//mostra os eventos
                    System.out.println("Evento " + (i + 1) + ": " + "Data e hora: " + evento.getDataHora() +" / " + "Endereço: " + evento.getEndereco() + " / " + "Descrição: "+ evento.getDescricao());
                    
                }	
            }
            
        }
    }
    
       
    
    //metodo para salvar os eventos no .data
    private void salvarEventos() {
    	try(ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("events.data"))){
    		outputStream.writeObject(eventos);// salva os eventos no arquivo .data
    	}catch (IOException e) {
    		System.out.println("Erro ao salvar os eventos: " + e.getMessage());
    	}
    }
    //carreagr os eventos do .data
    @SuppressWarnings("unchecked")
    
    private void carregarEventos() {
        File file = new File("events.data");// se nao tiver o arquivo cria um novo
        if (file.exists()) {// se o arquivo existe
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
                eventos = (List<Evento>) inputStream.readObject();// le o arquivo .data
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erro ao carregar os eventos: " + e.getMessage());
            }
        } else {
            System.out.println("O arquivo de eventos não existe. Será criado um novo arquivo.");
            eventos = new ArrayList<>(); // Inicializa a lista de eventos
        }
    }
    
    public void verificarEventoAtual() {
    	LocalDateTime agora = LocalDateTime.now();
    	boolean eventoOcorrendo = false;
    	boolean eventoJaOcorreu = false;
    	
    	for (Evento evento : eventos) {
    		LocalDateTime dataHoraEvento = evento.getDataHora();
    		if (agora.isAfter(dataHoraEvento) && agora.isBefore(dataHoraEvento.plusHours(1))) {
    			System.out.println("Um evento esta ocorrendo no momento: ");
    			System.out.println(evento.toString());
    			eventoOcorrendo = true;
    			break;
    			
    		}else if (agora.isAfter(dataHoraEvento)) {
    			eventoJaOcorreu = true;
    			System.out.println("O evento a seguir ja ocorreu: ");
    			System.out.println(evento.toString());
    		}
    	}
    	
    	if (!eventoOcorrendo && eventoJaOcorreu) {
    		System.out.println("Nenhum evento está ocorrendo no momento, mas alguns eventos já ocorreram.");
    	}else if (!eventoOcorrendo) {
            System.out.println("Nenhum evento está ocorrendo neste momento e nenhum evento já ocorreu.");
    }
    }
    
    private void presencaEvento(Scanner scanner) {
    	System.out.println("\nEscolha um evento futuro:");
    	if(eventos.isEmpty()) {
    		System.out.println("Nenhum evento cadastrado.");
    	}else {
    		System.out.println("Escolha um evento futuro para confirmar sua presença.");
    		for(int i = 0; i <eventos.size(); i++) {
    			Evento evento = eventos.get(i);
    			if(evento.getDataHora().isAfter(LocalDateTime.now())) {
    			System.out.println((i+1) + ". " + evento);
    		}
    	}
    	System.out.print("Digite o número do evento: ");
    	int numeroEvento = scanner.nextInt();
    	scanner.nextLine();
    	
    	if (numeroEvento >= 1 && numeroEvento <= eventos.size()) {
    		Evento eventoEscolhido = eventos.get(numeroEvento -1);
    		
    		eventoEscolhido.adicionarParticipante(this.email);
    		System.out.println("Presença confirmada no evento: " + eventoEscolhido.getDescricao());
    		salvarEventos();
    		
    	}
    }
    }
    private void listarMeusEventos() {
    	System.out.println("\nEventos que me cadastrei:");
    	boolean eventosEncontrados = false;
    	for (int i = 0; i < eventos.size(); i++) {
            Evento evento = eventos.get(i);
            if (evento.getParticipantes().contains(this.email)) {
                System.out.println((i + 1) + ". " + evento); // Adicione a numeração do evento
                eventosEncontrados = true;
    			
    		}
    	}
    	if (!eventosEncontrados) {
    		System.out.println("Voce não esta cadastrado em nenhum evento");
    	}
    }
    
    private void cancelarPresenca(Scanner scanner) {
    	System.out.println("\nCancelar presença em um evento");
    	listarMeusEventos();
    	
    	if(!eventos.isEmpty()){
    		System.out.print("Digite o número do evento que deseja cancelar presença: ");
    		int numeroEvento = scanner.nextInt();
    		scanner.nextLine();
    		
    		if (numeroEvento >= 1 && numeroEvento <= eventos.size()) {
    			Evento evento = eventos.get(numeroEvento - 1);
    			evento.removerParticipante(this.email);
    			salvarEventos();
    			System.out.println("\nPresença retirada.");
    			
    		}else {
    			System.out.println("Você não está cadastrado neste evento.");
    		}
    	}else {
            System.out.println("Número de evento inválido.");
        }
    	
    } 

    public static void menu(Scanner scanner) {
    	carregarUsuario();// carrega os usuarios cadastrados
        
        CadastroUsuario cadastro = new CadastroUsuario();// cria uma nova instancia da classe
//carregar os eventos
        cadastro.carregarEventos();
    	int opcao = -1;
        do {
            if (!cadastro.logado) { // Verificando se o usuário não está logado
            	System.out.println("\nMenu:");
                System.out.println("1. Cadastro");
                System.out.println("2. Login");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");
                opcao = scanner.nextInt();
                scanner.nextLine(); // Limpar o buffer

                switch (opcao) {
                    case 1:
                        System.out.println("\nCadastro de Usuário");
                        System.out.print("Digite seu nome: ");
                        String nome = scanner.nextLine();
                        System.out.print("Digite seu email: ");
                        String email = scanner.nextLine();
                        System.out.print("Digite sua senha: ");
                        String senha = scanner.nextLine();
                        cadastro.cadastrarUsuario(nome, email, senha);// armazena os dados que vc digitou e salva no metodo cadastrarUsuario
                        salvarUsuarios();
                        
                        break;
                    case 2:
                        System.out.println("\nLogin");
                        System.out.print("Digite seu email: ");
                        String emailLogin = scanner.nextLine();
                        System.out.print("Digite sua senha: ");
                        String senhaLogin = scanner.nextLine();
                        if (cadastro.realizarLogin(emailLogin, senhaLogin)) {
                            System.out.println("Login realizado com sucesso!");
                            
                            cadastro.exibirDetalhesUsuario();// exibe os detalhes do usuario
                            cadastro.exibirMenuEventos(scanner); // exibe o menu de eventos caso login bem sucedido
                        } else {
                            System.out.println("Falha no login. Verifique suas credenciais.");
                        }
                        break;
                    case 0:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            
            }
            
        } while (opcao != 0);
    	
        scanner.nextLine(); // Limpar o buffer
        cadastro.salvarEventos();// salva os eventos cadastrados
    }
    
    public static void main(String[] args) {
    	Scanner scanner = new Scanner(System.in);
    	menu(scanner);
    	
        scanner.close();

    }
}
