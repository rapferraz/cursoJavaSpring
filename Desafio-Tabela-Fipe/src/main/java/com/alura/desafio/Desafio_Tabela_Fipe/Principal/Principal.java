package com.alura.desafio.Desafio_Tabela_Fipe.Principal;

import com.alura.desafio.Desafio_Tabela_Fipe.model.Dados;
import com.alura.desafio.Desafio_Tabela_Fipe.model.Modelos;
import com.alura.desafio.Desafio_Tabela_Fipe.model.Veiculo;
import com.alura.desafio.Desafio_Tabela_Fipe.service.ConsomeApi;
import com.alura.desafio.Desafio_Tabela_Fipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

        private Scanner leitura = new Scanner(System.in);
        private ConsomeApi usaApi = new ConsomeApi();
        private ConverteDados conversor = new ConverteDados();

        private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";


    public void exibeMenu() {
        System.out.println("Quais veículos você deseja buscar (carros, motos, caminhoes): ");
        var opcao = leitura.nextLine();
        String endereco = "";

        if (opcao.toLowerCase().contains("carr")) {
                endereco = URL_BASE + "carros/marcas";
        } else if (opcao.toLowerCase().contains("mot")) {
            endereco = URL_BASE + "motos/marcas";
        } else if (opcao.toLowerCase().contains("caminh")) {
            endereco = URL_BASE + "caminhoes/marcas";
        }

        var json = usaApi.obterDados(endereco);
//        System.out.println(json);
        var marcas = conversor.obterLista(json, Dados.class);
        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Digite o código da marca: ");
        var codigoMarca = leitura.nextLine();

        endereco = endereco + "/" + codigoMarca + "/modelos";
        json = usaApi.obterDados(endereco);
        var modelosLista = conversor.obterDados(json, Modelos.class);

        System.out.println("\nModelos dessa marca: ");
        modelosLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("\n Digite um trecho do nome do veiculo a ser buscado: ");
        var nomeVeiculo = leitura.nextLine();

        List<Dados> modelosFiltrados = modelosLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\nModelos Filtrados: ");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("\nDigite o código do modelo desejado: ");
        var codigoModelo = leitura.nextLine();

        endereco = endereco + "/" + codigoModelo + "anos";
        json = usaApi.obterDados(endereco);
        List<Dados> anos = conversor.obterLista(json, Dados.class);
        List<Veiculo> veiculos = new ArrayList<>();

        for (Dados ano : anos) {
            var enderecoAnos = endereco + "/" + ano.codigo();
            json = usaApi.obterDados(enderecoAnos);
            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("\nTodos os veículos filtrados: ");
        veiculos.forEach(System.out::println);
    }
}