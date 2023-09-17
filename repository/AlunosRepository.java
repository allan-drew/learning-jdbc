package jdbc.repository;

import jdbc.conexao.ConnectionFactory;
import jdbc.dominio.Alunos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
As classes de implementação do repositório de banco de dados em Java são aquelas que fornecem a implementação concreta das operações do repositório.
Essas operações geralmente incluem a manipulação de dados no banco de dados, como inserir, atualizar, excluir e recuperar registros.
*/

public class AlunosRepository {

    public static void save(Alunos aluno) {
        String sql = "INSERT INTO `universidade`.`alunos` (`nome`) VALUES ('%s');".formatted(aluno.getNome());
        // Passamos o aluno01 como argumento para save no main
        // Com isso, substituimos o %s pelo nome do aluno (aluno.getNome()), que no caso é Raphael

        /* O "try-with-resources" é usado para simplificar a gestão de recursos que precisam ser fechados ao final de sua utilização,
        como conexões de banco de dados, arquivos e streams. */
        try (Connection connection = ConnectionFactory.getConnection();
             Statement stmt = connection.createStatement())
        // 1) pegando a conexao
        // 2) Em Java, java.sql.Statement é uma interface utilizada para enviar instruções SQL a um banco de dados
        {
            // Executa a instrução SQL (INSERT, UPDATE, DELETE ou instrução SQL DDL)
            int linhasAfetadas = stmt.executeUpdate(sql);  // além disso, retorna a quantidade de linhas das instrucoes sql realizadas ou retorna 0
            System.out.println(linhasAfetadas);
            System.out.println("O(A) aluno(a) " + aluno.getNome() + " foi adicionado(a) no D.B");

            // Precisamos fechar tanto a connection quanto o stmt.
            // Ao utilizar o "try-with-resources", o Java garante o fechamento dos recursos.
            // O uso do "try-with-resources" também melhora a legibilidade do código, uma vez que elimina a necessidade de escrever blocos finally para fechar os recursos.

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void delete(int id) {

        String sql = "DELETE FROM `universidade`.`alunos` WHERE (`id` = '%d');".formatted(id);

        try (Connection connection = ConnectionFactory.getConnection();
             Statement stmt = connection.createStatement()) {
            int linhasAfetadas = stmt.executeUpdate(sql);
            System.out.println(linhasAfetadas);
            System.out.println("O(A) aluno(a) " + id + " foi deletado(a) do D.B");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void update(Alunos aluno) {

        String sql = "UPDATE `universidade`.`alunos` SET `nome` = '%s' WHERE (`id` = '%d');".formatted(aluno.getNome(), aluno.getId());
        // ** A cláusula WHERE especifica quais registros devem ser atualizados. **
        // Se você omitir a cláusula WHERE, todos os registros das colunas especificadas serão atualizados! CUIDADO!

        try (Connection connection = ConnectionFactory.getConnection();
             Statement stmt = connection.createStatement()) {
            int linhasAfetadas = stmt.executeUpdate(sql);
            System.out.println(linhasAfetadas);
            System.out.println("O(A) aluno(a) de id " + aluno.getId() + " foi atualizado(a) no D.B");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public static List<Alunos> buscarPeloNome(String nomeParaBuscar) {

        // O operador LIKE é utilizado em uma cláusula WHERE para pesquisar um padrão especificado
        // em uma coluna por meio da utilização de caracteres curingas (wildcards). Caractere curinga é utilizado para substituir um ou mais caracteres em uma string (cadeia de caracteres).
        String sql = "select * from universidade.alunos where nome like '%s';".formatted("%" + nomeParaBuscar + "%"); // % ⇒ substitui caracteres;


        List<Alunos> alunosList = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) // ResultSet é uma interface em Java que fornece métodos para acessar
        // e manipular os resultados de uma consulta em um banco de dados.
        {
            while (resultSet.next()) {
                Alunos alunoParaColocarNaLista = Alunos.AlunosBuilder.builder()
                        .nome(resultSet.getString("nome"))
                        .id(resultSet.getInt("id"))
                        .build();
                alunosList.add(alunoParaColocarNaLista);
            }
        } catch (SQLException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
        }

        return alunosList;

    }

    public static List<Alunos> buscarTodosOsRegistros() {
        return buscarPeloNome("");
    }


    public static List<Alunos> buscarPeloNomeTesteSqlInjection(String nomeParaBuscar) {

        String sql = "select * from universidade.alunos where nome like '%s';".formatted(nomeParaBuscar);

        List<Alunos> alunosList = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                Alunos alunoParaColocarNaLista = Alunos.AlunosBuilder.builder()
                        .nome(resultSet.getString("nome"))
                        .id(resultSet.getInt("id"))
                        .build();
                alunosList.add(alunoParaColocarNaLista);
            }
        } catch (SQLException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
        }

        return alunosList;

    }

    //------------------------------------------------------------------------------------------
    // Usar a interface PreparedStatement em JDBC traz vantagens como:
    // - prevenção contra injeção de SQL (os parâmetros são tratados como valores e não podem ser interpretados como comandos SQL, logo, protege contra injeção de sql)
    // - desempenho
    // - facilidade (permite a substituição de parâmetros usando placeholders (?))
    // - integração com ORMs e Frameworks de Persistência

    public static List<Alunos> buscarPeloNomePreparedStatement(String nomeParaBuscar) {


        List<Alunos> alunosList = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement pstmt = preparedStatementBuscarPeloNome(connection, nomeParaBuscar);
             ResultSet resultSet = pstmt.executeQuery()) {
            while (resultSet.next()) {
                Alunos alunoParaColocarNaLista = Alunos.AlunosBuilder.builder()
                        .nome(resultSet.getString("nome"))
                        .id(resultSet.getInt("id"))
                        .build();
                alunosList.add(alunoParaColocarNaLista);
            }
        } catch (SQLException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
        }

        return alunosList;

    }

    // a responsabilidade de criar o PreparedStatement foi passada para o método preparedStatementBuscarPeloNome
    private static PreparedStatement preparedStatementBuscarPeloNome(Connection connection, String nome) throws SQLException {
        String sql = "select * from universidade.alunos where nome like ?;";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, String.format("%%%s%%", nome));
        return pstmt;

    }
    //------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------
    // UPDATE com PreparedStatement:
    public static void updatePreparedStatement(Alunos aluno) {

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement pstmt = preparedStatementUpdate(connection, aluno)) {
            int linhasAfetadas = pstmt.executeUpdate();
            System.out.println(linhasAfetadas);
            System.out.println("O(A) aluno(a) de id " + aluno.getId() + " foi atualizado(a) no D.B");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // a responsabilidade de criar o PreparedStatement foi passada para o método preparedStatementUpdate
    private static PreparedStatement preparedStatementUpdate(Connection connection, Alunos aluno) throws SQLException {

        String sql = "UPDATE `universidade`.`alunos` SET `nome` = ? WHERE (`id` = ?);";

        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, aluno.getNome());
        pstmt.setInt(2, aluno.getId());
        return pstmt;

    }
    //------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------
    /*
    TRANSAÇÃO:
    As transações em banco de dados devem possuir algumas propriedades, conhecidas como propriedades ACID.
    A = Atomicidade; C = Consistência; I = Isolamento; D = Durabilidade.

    Uma transação é uma unidade de processamento atômica (atomicidade) que deve ser executada integralmente até o fim
    ou não deve ser executada de maneira alguma – ** é tudo ou nada. **

    Se tudo ocorrer bem, as operações de gravação de uma transação devem ser confirmadas – o que chamamos de ** COMMIT **;
    Caso ocorra alguma falha, as operações de gravação de uma transação devem ser desfeitas – o que chamamos de ** ROLLBACK **.
     */

    // Para o caso do exemplo abaixo, vamos salvar uma lista de alunos no banco de dados.
    // Caso algum deles dê algum problema na inserção, nenhum será incluído no banco de dados.

    public static void saveTransaction(List<Alunos> alunosList) {

        try (Connection connection = ConnectionFactory.getConnection()) {

            connection.setAutoCommit(false);

            preparedStatementSaveTransaction(connection, alunosList);
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void preparedStatementSaveTransaction(Connection connection, List<Alunos> alunosList) throws SQLException {

        String sql = "INSERT INTO `universidade`.`alunos` (`nome`) VALUES (?);";

        boolean shouldRollback = false;

        for (Alunos a : alunosList) {
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                System.out.println("Salvando aluno " + a.getNome());
                pstmt.setString(1, a.getNome());

                // simulando exceção para ser verificado o funcionamento da transaction
                if(a.getNome().equals("Leticia")) throw new SQLException("Erro ao adicionar Leticia");

                pstmt.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                shouldRollback = true;
            }
        }

        // Caso ocorra alguma falha, as operações de gravação de uma transação devem ser desfeitas – o que chamamos de ROLLBACK.
        if (shouldRollback) {
            System.out.println("**************** Rollback na transação! OPERAÇÕES DESFEITAS! ****************");
            connection.rollback();
        }

    }



}
