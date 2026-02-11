package com.exemplo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import spark.Spark;

public class ClienteWeb {
    public static void main(String[] args) {
        try {
            // Configurações de Rede
            String serverHost = System.getenv("SERVER_HOST");
            if (serverHost == null) serverHost = "localhost";

            Registry registry = LocateRegistry.getRegistry(serverHost, 1099);
            IAutenticacao stub = (IAutenticacao) registry.lookup("AuthService");

            // Permite acesso externo e define porta
            Spark.ipAddress("0.0.0.0");
            Spark.port(8080);

            // --- TELA PRINCIPAL (GET /) ---
            Spark.get("/", (req, res) -> renderPage());

            // --- PROCESSAR LOGIN (POST /login) ---
            Spark.post("/login", (req, res) -> {
                String u = req.queryParams("usuario");
                String s = req.queryParams("senha");
                boolean sucesso = stub.login(u, s);

                if (sucesso) {
                    return renderResult("Bem-vindo(a)!", "Olá, <strong>" + u + "</strong>. Login realizado com sucesso!", "success");
                } else {
                    return renderResult("Acesso Negado", "Usuário ou senha incorretos.", "danger");
                }
            });

            // --- PROCESSAR CADASTRO (POST /cadastrar) ---
            Spark.post("/cadastrar", (req, res) -> {
                String u = req.queryParams("usuario");
                String s = req.queryParams("senha");
                boolean sucesso = stub.cadastrar(u, s);

                if (sucesso) {
                    return renderResult("Conta Criada!", "Seu cadastro foi realizado. Faça login agora.", "success");
                } else {
                    return renderResult("Falha no Cadastro", "Não foi possível cadastrar. O usuário já existe?", "warning");
                }
            });

            System.out.println(">>> Servidor Web (Http.cat Edition) rodando em http://0.0.0.0:8080 <<<");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método auxiliar para gerar o HTML da página de Login/Cadastro
    private static String renderPage() {
        return """
            <!doctype html>
            <html lang="pt-br">
              <head>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <title>Sistema Distribuído</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
                <style>
                    body { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
                    .card { border: none; border-radius: 15px; }
                    .btn-primary { background-color: #667eea; border: none; }
                    .btn-primary:hover { background-color: #5a6fd6; }
                </style>
              </head>
              <body class="d-flex align-items-center min-vh-100 py-5">
                
                <div class="container">
                  <div class="row justify-content-center">
                    <div class="col-lg-10">
                      <div class="card shadow-lg overflow-hidden">
                        <div class="row g-0">
                          
                          <div class="col-md-6 bg-white p-5">
                            <h2 class="fw-bold mb-4 text-primary">Login</h2>
                            <form action="/login" method="POST">
                              <div class="form-floating mb-3">
                                <input type="text" class="form-control" id="loginUser" name="usuario" placeholder="Usuario" required>
                                <label for="loginUser">Usuário</label>
                              </div>
                              <div class="form-floating mb-3">
                                <input type="password" class="form-control" id="loginPass" name="senha" placeholder="Senha" required>
                                <label for="loginPass">Senha</label>
                              </div>
                              <div class="d-grid">
                                <button type="submit" class="btn btn-primary btn-lg">Entrar</button>
                              </div>
                            </form>
                          </div>

                          <div class="col-md-6 bg-light p-5 border-start">
                            <h2 class="fw-bold mb-4 text-secondary">Novo Aqui?</h2>
                            <p class="text-muted mb-4">Crie sua conta para acessar os recursos.</p>
                            <form action="/cadastrar" method="POST">
                              <div class="form-floating mb-3">
                                <input type="text" class="form-control" id="cadUser" name="usuario" placeholder="Novo Usuario" required>
                                <label for="cadUser">Escolha um Usuário</label>
                              </div>
                              <div class="form-floating mb-3">
                                <input type="password" class="form-control" id="cadPass" name="senha" placeholder="Nova Senha" required>
                                <label for="cadPass">Crie uma Senha</label>
                              </div>
                              <div class="d-grid">
                                <button type="submit" class="btn btn-outline-success btn-lg">Cadastrar-se</button>
                              </div>
                            </form>
                          </div>

                        </div>
                      </div>
                      <div class="text-center mt-4 text-white opacity-75">
                        <small>Powered by Java RMI & Http.cat</small>
                      </div>
                    </div>
                  </div>
                </div>
              </body>
            </html>
        """;
    }

    private static String renderResult(String titulo, String mensagem, String tipo) {

        String statusCode;
        String corTexto;

        if (tipo.equals("success")) {
            statusCode = "200";
            corTexto = "text-success";
        } else if (tipo.equals("warning")) {
            statusCode = "409";
            corTexto = "text-warning";
        } else {
            statusCode = "401";
            corTexto = "text-danger";
        }


        String catUrl = "https://http.cat/" + statusCode;

        return """
            <!doctype html>
            <html lang="pt-br">
              <head>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <title>%s</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
                <style>
                    body { background-color: #2c3e50; font-family: sans-serif; }
                    .card { border-radius: 15px; overflow: hidden; max-width: 450px; margin: auto; }
                    .cat-img { width: 100%%; height: auto; display: block; }
                    .btn-back { background-color: #34495e; color: white; border: none; }
                    .btn-back:hover { background-color: #2c3e50; color: white; }
                </style>
              </head>
              <body class="d-flex align-items-center min-vh-100 p-3">
                
                <div class="card shadow-lg">
                    <img src="%s" alt="Status Cat" class="cat-img">
                    
                    <div class="card-body text-center p-4">
                      <h2 class="fw-bold mb-2 %s">%s</h2>
                      <p class="text-muted mb-4">%s</p>
                      
                      <a href="/" class="btn btn-back w-100 py-2 d-flex align-items-center justify-content-center gap-2">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left" viewBox="0 0 16 16">
                          <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z"/>
                        </svg>
                        Voltar
                      </a>
                    </div>
                </div>

              </body>
            </html>
        """.formatted(titulo, catUrl, corTexto, titulo, mensagem);
    }
}