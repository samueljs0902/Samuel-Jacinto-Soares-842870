import java.sql.*;
import java.util.*;

// ===============================
// CONEXÃO COM BANCO (SQLITE)
// ===============================
class DBConnection {
    private static final String URL = "jdbc:sqlite:apostas.db";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL);
    }
}

// ===============================
// INICIALIZAÇÃO DO BANCO
// ===============================
class DBInit {
    public static void init() {
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement()) {

            st.execute("CREATE TABLE IF NOT EXISTS clube (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT)");
            st.execute("CREATE TABLE IF NOT EXISTS participante (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT)");
            st.execute("""
                CREATE TABLE IF NOT EXISTS partida (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    timeA TEXT,
                    timeB TEXT,
                    golsA INTEGER,
                    golsB INTEGER,
                    finalizada BOOLEAN
                )
            """);

            System.out.println("Banco pronto!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// ===============================
// INTERFACE
// ===============================
interface Pontuavel {
    int calcularPontos();
}

// ===============================
// CLUBE
// ===============================
class Clube {
    private String nome;

    public Clube(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}

// ===============================
// PARTIDA
// ===============================
class Partida {
    private Clube timeA;
    private Clube timeB;
    private int golsA;
    private int golsB;
    private boolean finalizada;

    public Partida(Clube a, Clube b) {
        this.timeA = a;
        this.timeB = b;
    }

    public void registrarResultado(int a, int b) {
        golsA = a;
        golsB = b;
        finalizada = true;
    }

    public boolean isFinalizada() { return finalizada; }
    public int getGolsA() { return golsA; }
    public int getGolsB() { return golsB; }

    public String info() {
        return timeA.getNome() + " x " + timeB.getNome();
    }

    public Clube getTimeA() { return timeA; }
    public Clube getTimeB() { return timeB; }
}

// ===============================
// APOSTA
// ===============================
class Aposta implements Pontuavel {
    private Partida partida;
    private int a, b;

    public Aposta(Partida p, int a, int b) {
        this.partida = p;
        this.a = a;
        this.b = b;
    }

    public int calcularPontos() {
        if (!partida.isFinalizada()) return 0;

        boolean resultado =
            (a > b && partida.getGolsA() > partida.getGolsB()) ||
            (b > a && partida.getGolsB() > partida.getGolsA()) ||
            (a == b && partida.getGolsA() == partida.getGolsB());

        boolean exato =
            (a == partida.getGolsA() && b == partida.getGolsB());

        if (resultado && exato) return 10;
        if (resultado) return 5;
        return 0;
    }
}

// ===============================
// PARTICIPANTE
// ===============================
class Participante {
    private String nome;
    private List<Aposta> apostas = new ArrayList<>();

    public Participante(String nome) {
        this.nome = nome;
    }

    public void apostar(Partida p, int a, int b) {
        apostas.add(new Aposta(p, a, b));
    }

    public int getPontos() {
        int t = 0;
        for (Aposta a : apostas) t += a.calcularPontos();
        return t;
    }

    public String getNome() { return nome; }
}

// ===============================
// GRUPO
// ===============================
class Grupo {
    private String nome;
    private List<Participante> participantes = new ArrayList<>();

    public Grupo(String nome) {
        this.nome = nome;
    }

    public void add(String nome) {
        participantes.add(new Participante(nome));
    }

    public List<Participante> getLista() {
        return participantes;
    }

    public void ranking() {
        participantes.sort((a, b) ->
            Integer.compare(b.getPontos(), a.getPontos())
        );

        System.out.println("\nRanking:");
        int i = 1;
        for (Participante p : participantes) {
            System.out.println(i++ + " - " + p.getNome() +
                " (" + p.getPontos() + " pts)");
        }
    }
}

// ===============================
// CAMPEONATO
// ===============================
class Campeonato {
    private List<Clube> clubes = new ArrayList<>();
    private List<Partida> partidas = new ArrayList<>();

    public void addClube(String nome) {
        clubes.add(new Clube(nome));
    }

    public void criar(int a, int b) {
        partidas.add(new Partida(clubes.get(a), clubes.get(b)));
    }

    public Partida get(int i) {
        return partidas.get(i);
    }

    public List<Partida> getPartidas() {
        return partidas;
    }
}

// ===============================
// DAO - CLUBE
// ===============================
class ClubeDAO {
    public void salvar(Clube c) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO clube(nome) VALUES(?)")) {

            ps.setString(1, c.getNome());
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// ===============================
// DAO - PARTICIPANTE
// ===============================
class ParticipanteDAO {
    public void salvar(Participante p) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO participante(nome) VALUES(?)")) {

            ps.setString(1, p.getNome());
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// ===============================
// DAO - PARTIDA
// ===============================
class PartidaDAO {
    public void salvar(Partida p) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO partida(timeA,timeB,golsA,golsB,finalizada) VALUES(?,?,?,?,?)")) {

            ps.setString(1, p.getTimeA().getNome());
            ps.setString(2, p.getTimeB().getNome());
            ps.setInt(3, p.getGolsA());
            ps.setInt(4, p.getGolsB());
            ps.setBoolean(5, p.isFinalizada());

            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// ===============================
// MAIN
// ===============================
public class Main {
    public static void main(String[] args) {

        DBInit.init();

        Campeonato c = new Campeonato();

        c.addClube("Flamengo");
        c.addClube("Palmeiras");
        c.addClube("Corinthians");

        c.criar(0, 1);

        Grupo g = new Grupo("Amigos");

        g.add("Joao");
        g.add("Maria");

        // apostas
        g.getLista().get(0).apostar(c.get(0), 2, 1);
        g.getLista().get(1).apostar(c.get(0), 1, 1);

        // resultado
        c.get(0).registrarResultado(2, 1);

        // salvar banco
        ClubeDAO clubeDAO = new ClubeDAO();
        for (Partida p : c.getPartidas()) {
            new PartidaDAO().salvar(p);
        }

        for (Participante p : g.getLista()) {
            new ParticipanteDAO().salvar(p);
        }

        // ranking
        g.ranking();
    }
}
