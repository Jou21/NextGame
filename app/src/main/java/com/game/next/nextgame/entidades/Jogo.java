
package com.game.next.nextgame.entidades;

public class Jogo {
    private String nome;
    private String preco;
    private String urlImgJogo;
    private String descricao;
    private String rating;
    private String categoria;
    private String faixaEtaria;
    private String multiplayer;
    private String dataLancamento;
    private String sku;
    private String urlImgComplementar1;
    private String urlImgComplementar2;
    private String urlImgComplementar3;
    private String urlImgComplementar4;
    private String urlImgComplementar5;
    private String urlVideo;

    public Jogo(String nome, String preco, String urlImgJogo, String descricao,
            String rating, String categoria, String faixaEtaria, String multiplayer, String dataLancamento,
            String sku, String urlImgComplementar1, String urlImgComplementar2, String urlImgComplementar3,
            String urlImgComplementar4, String urlImgComplementar5, String urlVideo) {
        this.nome = nome;
        this.preco = preco;
        this.urlImgJogo = urlImgJogo;
        this.descricao = descricao;
        this.rating = rating;
        this.categoria = categoria;
        this.faixaEtaria = faixaEtaria;
        this.multiplayer = multiplayer;
        this.dataLancamento = dataLancamento;
        this.sku = sku;
        this.urlImgComplementar1 = urlImgComplementar1;
        this.urlImgComplementar2 = urlImgComplementar2;
        this.urlImgComplementar3 = urlImgComplementar3;
        this.urlImgComplementar4 = urlImgComplementar4;
        this.urlImgComplementar5 = urlImgComplementar5;
        this.urlVideo = urlVideo;
    }
    public Jogo() {
        
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getUrlImgJogo() {
        return urlImgJogo;
    }

    public void setUrlImgJogo(String urlImgJogo) {
        this.urlImgJogo = urlImgJogo;
    }
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getFaixaEtaria() {
        return faixaEtaria;
    }

    public void setFaixaEtaria(String faixaEtaria) {
        this.faixaEtaria = faixaEtaria;
    }

    public String getMultiplayer() {
        return multiplayer;
    }

    public void setMultiplayer(String multiplayer) {
        this.multiplayer = multiplayer;
    }

    public String getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(String dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getUrlImgComplementar1() {
        return urlImgComplementar1;
    }

    public void setUrlImgComplementar1(String urlImgComplementar1) {
        this.urlImgComplementar1 = urlImgComplementar1;
    }

    public String getUrlImgComplementar2() {
        return urlImgComplementar2;
    }

    public void setUrlImgComplementar2(String urlImgComplementar2) {
        this.urlImgComplementar2 = urlImgComplementar2;
    }

    public String getUrlImgComplementar3() {
        return urlImgComplementar3;
    }

    public void setUrlImgComplementar3(String urlImgComplementar3) {
        this.urlImgComplementar3 = urlImgComplementar3;
    }

    public String getUrlImgComplementar4() {
        return urlImgComplementar4;
    }

    public void setUrlImgComplementar4(String urlImgComplementar4) {
        this.urlImgComplementar4 = urlImgComplementar4;
    }

    public String getUrlImgComplementar5() {
        return urlImgComplementar5;
    }

    public void setUrlImgComplementar5(String urlImgComplementar5) {
        this.urlImgComplementar5 = urlImgComplementar5;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }
}