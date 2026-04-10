import java.util.*;

// INTERFACE
interface Pontuavel {
    int calcularPontos();
}

// CLUBE
class Clube {
    private String nome;

    public Clube() {}

    public Clube(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}

// PARTIDA
class Partida {
    private Clube timeA;
    private Clube timeB;
    private int golsA;
    private int golsB;
    private boolean finalizada;

    public Partida(Clube a, Clube b) {
        this.timeA = a;
        this.timeB = b;
        this.golsA = 0;
        this.golsB = 0;
        this.finalizada = false;
    }

    public void registrarResultado(int a, int b) {
        this.golsA = a;
        this.golsB = b;
        this.finalizada = true;
    }

    public String getResultado() {
        if (golsA > golsB) return timeA.getNome();
        if (golsB > golsA) return timeB.getNome();
        return "Empate";
    }

    public int getGolsA() { return golsA; }
    public int getGolsB() { return golsB; }
    public boolean isFinalizada() { return finalizada; }

    public String info() {
        return timeA.getNome() + " x " + timeB.getNome();
    }
}

// APOSTA
class Aposta implements Pontuavel {
    private Partida partida;
    private int palpiteA;
    private int palpiteB;

    public Aposta(Partida p, int a, int b) {
        this.partida = p;
        this.palpiteA = a;
        this.palpiteB = b;
    }

    @Override
    public int calcularPontos() {
        if (!partida.isFinalizada()) return 0;

        boolean resultadoCorreto = false;
        boolean placarExato = false;

        if ((palpiteA > palpiteB && partida.getGolsA() > partida.getGolsB()) ||
            (palpiteB > palpiteA && partida.getGolsB() > partida.getGolsA()) ||
            (palpiteA == palpiteB && partida.getGolsA() == partida.getGolsB())) {
            resultadoCorreto = true;
        }

        if (palpiteA == partida.getGolsA() && palpiteB == partida.getGolsB()) {
            placarExato = true;
        }

        if (resultadoCorreto && placarExato) return 10;
        if (resultadoCorreto) return 5;
        return 0;
    }
}

// PARTICIPANTE
class Participante {
    private String nome;
    private List<Aposta> apostas;

    public Participante(String nome) {
        this.nome = nome;
        this.apostas = new ArrayList<>();
    }

    public void registrarAposta(Partida p, int a, int b) {
        apostas.add(new Aposta(p, a, b));
    }

    public int getPontuacao() {
        int total = 0;
        for (Aposta a : apostas) {
            total += a.calcularPontos();
        }
        return total;
    }

    public String getNome() {
        return nome;
    }
}

// GRUPO
class Grupo {
    private String nome;
    private List<Participante> participantes;

    public Grupo(String nome) {
        this.nome = nome;
        this.participantes = new ArrayList<>();
    }

    public boolean adicionarParticipante(String nome) {
        if (participantes.size() >= 5) return false;
        participantes.add(new Participante(nome));
        return true;
    }

    public List<Participante> getParticipantes() {
        return participantes;
    }

    public void mostrarClassificacao() {
        System.out.println("\nClassificação do Grupo: " + nome);
        for (Participante p : participantes) {
            System.out.println(p.getNome() + " - Pontos: " + p.getPontuacao());
        }
    }
}

// CAMPEONATO
class Campeonato {
    private String nome;
    private List<Clube> clubes;
    private List<Partida> partidas;

    public Campeonato(String nome) {
        this.nome = nome;
        this.clubes = new ArrayList<>();
        this.partidas = new ArrayList<>();
    }

    public boolean adicionarClube(String nome) {
        if (clubes.size() >= 8) return false;
        clubes.add(new Clube(nome));
        return true;
    }

    public Clube getClube(int i) {
        return clubes.get(i);
    }

    public void criarPartida(int a, int b) {
        partidas.add(new Partida(getClube(a), getClube(b)));
    }

    public Partida getPartida(int i) {
        return partidas.get(i);
    }
}

// MAIN
public class Main {
    public static void main(String[] args) {
        Campeonato camp = new Campeonato("Brasileirao");

        camp.adicionarClube("Flamengo");
        camp.adicionarClube("Palmeiras");
        camp.adicionarClube("Sao Paulo");
        camp.adicionarClube("Corinthians");

        camp.criarPartida(0, 1);
        camp.criarPartida(2, 3);

        Grupo grupo = new Grupo("Amigos");
        grupo.adicionarParticipante("Joao");
        grupo.adicionarParticipante("Maria");

        // Apostas
        grupo.getParticipantes().get(0).registrarAposta(camp.getPartida(0), 2, 1);
        grupo.getParticipantes().get(1).registrarAposta(camp.getPartida(0), 1, 1);

        // Resultados
        camp.getPartida(0).registrarResultado(2, 1);

        // Classificação
        grupo.mostrarClassificacao();
    }
}