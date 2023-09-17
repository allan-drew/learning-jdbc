package jdbc.dominio;

import java.util.Objects;

/*
As "classes de domínio" em Java se referem às classes que representam os conceitos e entidades fundamentais de um sistema ou aplicação.
Elas são utilizadas para modelar os dados e comportamentos relevantes para o domínio específico em que o software está sendo desenvolvido.

Para a classe de Alunos, vamos utilizar o padrão builder.
O padrão de projeto Builder é um dos padrões de criação (creational design patterns) em Java,
e é usado quando queremos criar objetos complexos com muitos parâmetros configuráveis.

 */

public class Alunos {

    private Integer id;
    private String nome;


    public static final class AlunosBuilder {
        private Integer id;
        private String nome;

        private AlunosBuilder() {
        }

        public static AlunosBuilder builder() {
            return new AlunosBuilder();
        }

        public AlunosBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public AlunosBuilder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public Alunos build() {
            Alunos alunos = new Alunos();
            alunos.id = this.id;
            alunos.nome = this.nome;
            return alunos;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alunos alunos = (Alunos) o;
        return Objects.equals(id, alunos.id) && Objects.equals(nome, alunos.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }


    @Override
    public String toString() {
        return "{ " + '\n' +
                "   id = " + id + '\n' +
                "   nome = " + nome + '\n' +
                '}';
    }

}
