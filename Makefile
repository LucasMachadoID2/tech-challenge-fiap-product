.PHONY: help start stop restart status logs port-forward build deploy clean start-local start-prod stop-local stop-prod

# Cores para output
GREEN := \033[0;32m
YELLOW := \033[1;33m
RED := \033[0;31m
BLUE := \033[0;34m
NC := \033[0m # No Color

# Variáveis
APP_NAME := app-product
SERVICE_NAME := app-product-service
NAMESPACE := default
PORT := 8081
ENV ?= prod

# Arquivos por ambiente
ifeq ($(ENV),local)
	ENV_LABEL := 💻 LOCAL
	ENV_COLOR := $(BLUE)
else
	ENV_LABEL := 🚀 PRODUÇÃO
	ENV_COLOR := $(RED)
endif

help: ## Mostra este menu de ajuda
	@echo "$(YELLOW)📚 Comandos disponíveis - Tech Challenge FIAP Product$(NC)"
	@echo ""
	@echo "$(BLUE)🌍 Comandos Gerais (usa ENV=local ou ENV=prod):$(NC)"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | grep -v "LOCAL\|PROD" | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "  $(GREEN)%-20s$(NC) %s\n", $$1, $$2}'
	@echo ""
	@echo "$(BLUE)💻 Comandos Específicos - LOCAL:$(NC)"
	@grep -E '^[a-zA-Z_-]+:.*?## .*LOCAL.*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "  $(GREEN)%-20s$(NC) %s\n", $$1, $$2}'
	@echo ""
	@echo "$(BLUE)🚀 Comandos Específicos - PRODUÇÃO:$(NC)"
	@grep -E '^[a-zA-Z_-]+:.*?## .*PROD.*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "  $(GREEN)%-20s$(NC) %s\n", $$1, $$2}'
	@echo ""
	@echo "$(YELLOW)💡 Exemplos de uso:$(NC)"
	@echo "  make start              # Inicia em LOCAL (padrão)"
	@echo "  make start ENV=prod     # Inicia em PRODUÇÃO"
	@echo "  make start-local        # Inicia em LOCAL (atalho)"
	@echo "  make start-prod         # Inicia em PRODUÇÃO (atalho)"
	@echo ""

start: ## Inicia a aplicação (ENV=local ou ENV=prod)
	@echo "$(ENV_COLOR)🚀 Iniciando aplicação - $(ENV_LABEL)$(NC)"
	@echo ""
	@echo "$(YELLOW)📦 1. Aplicando ConfigMaps...$(NC)"
	@kubectl apply -f k8s/configmap.yaml
	@kubectl apply -f k8s/mongodb-configmap.yaml
	@echo ""
	@echo "$(YELLOW)🔐 2. Aplicando Secrets...$(NC)"
	@kubectl apply -f k8s/secret.yaml
	@kubectl apply -f k8s/mongodb-secrets.yaml
	@echo ""
	@echo "$(YELLOW)🗄️  3. Aplicando MongoDB...$(NC)"
	@kubectl apply -f k8s/mongodb-deployment.yaml
	@kubectl apply -f k8s/mongodb-service.yaml
	@echo ""
	@echo "$(YELLOW)⏳ Aguardando MongoDB ficar pronto...$(NC)"
	@kubectl wait --for=condition=ready pod -l app=mongodb --timeout=120s || echo "MongoDB ainda inicializando..."
	@echo ""
	@echo "$(YELLOW)🏗️  4. Aplicando Aplicação...$(NC)"
	@kubectl apply -f k8s/app-deployment.yaml
	@kubectl apply -f k8s/app-service.yaml
	@echo ""
	@echo "$(YELLOW)📊 5. Aplicando HPA...$(NC)"
	@kubectl apply -f k8s/hpa.yaml
	@echo ""
	@echo "$(YELLOW)⏳ Aguardando pods da aplicação ficarem prontos...$(NC)"
	@sleep 5
	@for i in 1 2 3 4 5 6 7 8 9 10 11 12; do \
		POD_READY=$$(kubectl get pods -l app=app-product -o jsonpath='{.items[0].status.conditions[?(@.type=="Ready")].status}' 2>/dev/null || echo "False"); \
		if [ "$$POD_READY" = "True" ]; then \
			echo "$(GREEN)✅ Pods prontos!$(NC)"; \
			break; \
		fi; \
		echo "Aguardando... ($$i/12)"; \
		sleep 5; \
	done
	@kubectl get pods -l app=app-product
	@echo ""
	@POD_STATUS=$$(kubectl get pods -l app=app-product -o jsonpath='{.items[0].status.phase}' 2>/dev/null || echo "NotFound"); \
	if [ "$$POD_STATUS" != "Running" ]; then \
		echo "$(YELLOW)⚠️  Pods não estão Running. Status: $$POD_STATUS$(NC)"; \
		echo "$(YELLOW)Verificando eventos...$(NC)"; \
		kubectl describe pods -l app=app-product | grep -A 10 Events || true; \
	fi
	@echo ""
	@echo "$(GREEN)✅ Aplicação iniciada com sucesso - $(ENV_LABEL)!$(NC)"
	@echo ""
	@make status
	@echo ""
ifeq ($(ENV),local)
	@echo "$(GREEN)🔗 URLs disponíveis (após port-forward):$(NC)"
	@echo "  - Swagger UI:    http://localhost:$(PORT)/swagger-ui.html"
	@echo "  - API Docs:      http://localhost:$(PORT)/api-docs"
	@echo "  - Health Check:  http://localhost:$(PORT)/actuator/health"
	@echo ""
	@echo "$(YELLOW)💡 Para acessar localmente, execute:$(NC)"
	@echo "   make port-forward"
	@echo ""
else
	@echo "$(GREEN)✅ Aplicação disponível no cluster Kubernetes!$(NC)"
	@echo ""
endif

start-local: ## [LOCAL] Inicia a aplicação em ambiente local
	@make start ENV=local

start-prod: ## [PROD] Inicia a aplicação em ambiente de produção
	@make start ENV=prod

stop: ## Para e remove todos os recursos (ENV=local ou ENV=prod)
	@echo "$(ENV_COLOR)🛑 Parando aplicação - $(ENV_LABEL)$(NC)"
	@echo ""
	@kubectl delete -f k8s/hpa.yaml --ignore-not-found=true
	@kubectl delete -f k8s/app-service.yaml --ignore-not-found=true
	@kubectl delete -f k8s/app-deployment.yaml --ignore-not-found=true
	@kubectl delete -f k8s/mongodb-service.yaml --ignore-not-found=true
	@kubectl delete -f k8s/mongodb-deployment.yaml --ignore-not-found=true
	@kubectl delete -f k8s/mongodb-secrets.yaml --ignore-not-found=true
	@kubectl delete -f k8s/secret.yaml --ignore-not-found=true
	@kubectl delete -f k8s/mongodb-configmap.yaml --ignore-not-found=true
	@kubectl delete -f k8s/configmap.yaml --ignore-not-found=true
	@echo ""
	@echo "$(GREEN)✅ Aplicação parada - $(ENV_LABEL)!$(NC)"
	@echo ""
	@make status

stop-local: ## [LOCAL] Para a aplicação em ambiente local
	@make stop ENV=local

stop-prod: ## [PROD] Para a aplicação em ambiente de produção
	@make stop ENV=prod

restart: stop start ## Reinicia a aplicação (ENV=local ou ENV=prod)

restart-local: ## [LOCAL] Reinicia a aplicação em ambiente local
	@make restart ENV=local

restart-prod: ## [PROD] Reinicia a aplicação em ambiente de produção
	@make restart ENV=prod

status: ## Mostra o status dos pods e services
	@echo "$(YELLOW)📊 Status dos recursos:$(NC)"
	@echo ""
	@echo "$(YELLOW)Pods:$(NC)"
	@kubectl get pods
	@echo ""
	@echo "$(YELLOW)Services:$(NC)"
	@kubectl get svc
	@echo ""
	@echo "$(YELLOW)HPA:$(NC)"
	@kubectl get hpa || true

logs: ## Mostra os logs da aplicação (use ctrl+c para sair)
	@echo "$(YELLOW)📋 Logs da aplicação (ctrl+c para sair):$(NC)"
	@echo ""
	@kubectl logs -f deployment/$(APP_NAME)

logs-mongodb: ## Mostra os logs do MongoDB
	@echo "$(YELLOW)📋 Logs do MongoDB (ctrl+c para sair):$(NC)"
	@echo ""
	@kubectl logs -f deployment/mongodb

port-forward: ## Cria port-forward para acessar a aplicação localmente
	@echo "$(YELLOW)🌐 Criando port-forward...$(NC)"
	@echo ""
	@echo "$(GREEN)🔗 URLs disponíveis:$(NC)"
	@echo "  - Swagger UI:    http://localhost:$(PORT)/swagger-ui.html"
	@echo "  - API Docs:      http://localhost:$(PORT)/api-docs"
	@echo "  - Health Check:  http://localhost:$(PORT)/actuator/health"
	@echo ""
	@echo "$(YELLOW)⚠️  Pressione Ctrl+C para parar o port-forward$(NC)"
	@echo ""
	@kubectl port-forward service/$(SERVICE_NAME) $(PORT):80

build: ## Compila o projeto com Maven
	@echo "$(YELLOW)🔨 Compilando projeto...$(NC)"
	@mvn clean package -DskipTests
	@echo "$(GREEN)✅ Compilação concluída!$(NC)"

test: ## Executa os testes
	@echo "$(YELLOW)🧪 Executando testes...$(NC)"
	@mvn test
	@echo "$(GREEN)✅ Testes concluídos!$(NC)"

build-docker: ## Constrói a imagem Docker
	@echo "$(YELLOW)🐳 Construindo imagem Docker...$(NC)"
	@docker build -t gguihermegarcia1/tech-challenge-fiap-product:latest .
	@echo "$(GREEN)✅ Imagem Docker criada!$(NC)"

push-docker: build-docker ## Faz push da imagem Docker para o registry
	@echo "$(YELLOW)📤 Fazendo push da imagem...$(NC)"
	@docker push gguihermegarcia1/tech-challenge-fiap-product:latest
	@echo "$(GREEN)✅ Push concluído!$(NC)"

deploy: build-docker push-docker restart ## Build completo + push + restart

deploy-prod: ## [PROD] Build completo + push + restart em produção
	@make deploy ENV=prod

describe-pod: ## Descreve o pod da aplicação (útil para debug)
	@kubectl describe pod -l app=$(APP_NAME)

describe-mongodb: ## Descreve o pod do MongoDB
	@kubectl describe pod -l app=mongodb

shell-app: ## Abre shell no container da aplicação
	@kubectl exec -it deployment/$(APP_NAME) -- /bin/sh

shell-mongodb: ## [LOCAL] Abre shell no container do MongoDB (apenas local)
	@kubectl exec -it deployment/mongodb -- mongosh -u admin -p password123 --authenticationDatabase admin

clean: stop ## Remove tudo (pods, services, builds locais)
	@echo "$(YELLOW)🧹 Limpando ambiente...$(NC)"
	@mvn clean
	@echo "$(GREEN)✅ Ambiente limpo!$(NC)"

clean-local: ## [LOCAL] Remove tudo em ambiente local
	@make clean ENV=local

clean-prod: ## [PROD] Remove tudo em ambiente de produção
	@make clean ENV=prod

check: ## Verifica se o Kubernetes está funcionando
	@echo "$(YELLOW)🔍 Verificando ambiente...$(NC)"
	@echo ""
	@echo "$(YELLOW)Kubectl version:$(NC)"
	@kubectl version --client
	@echo ""
	@echo "$(YELLOW)Cluster info:$(NC)"
	@kubectl cluster-info
	@echo ""
	@echo "$(GREEN)✅ Ambiente OK!$(NC)"
