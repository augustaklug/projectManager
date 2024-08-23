# User story 1
Como um gerente de projeto, quero poder criar novos projetos, para acompanhar seu progresso.

## Critérios de aceitação

### Orientado a cenário

- [ ] Dada uma página inicial, quando eu clicar em “Novo Projeto”, espero ser redirecionado para um formulário de criação de projeto.
- [ ] Dada a página inicial, quando eu tentar criar um projeto com um nome que já existe, então espero receber uma mensagem de erro informando que o nome do projeto já está em uso.
- [ ] Dada a página de criação de projeto, quando eu não preencher todos os campos obrigatórios, então espero que os campos sejam destacados e uma mensagem de erro seja exibida solicitando o preenchimento completo do formulário.

### Orientado a regra

- [ ] O sistema deve validar que o nome do projeto é único e não duplicado em relação aos projetos existentes.
- [ ] O formulário de criação de projeto deve incluir campos para datas de início e término estimadas.
- [ ] O sistema deve permitir a seleção de membros da equipe que serão associados ao projeto.
- [ ] Após a criação, o sistema deve gerar automaticamente um ID de projeto que pode ser usado para referência futura.

---

# User story 2
Como um membro da equipe, quero poder visualizar as tarefas atribuídas a mim, para saber quais atividades eu devo concluir.

## Critérios de aceitação

### Orientado a cenário

- [ ] Dada a minha página de perfil, quando eu clicar em “Minhas Tarefas”, espero ver uma lista de tarefas atribuídas a mim.
- [ ] Dada a minha página de perfil, quando eu clicar em uma tarefa na lista “Minhas Tarefas”, espero ser redirecionado para a página de detalhes da tarefa.
- [ ] Dada a minha página de perfil, quando eu clicar em “Minhas Tarefas” e não tiver tarefas atribuídas a mim, espero ver uma mensagem informando que não há tarefas atribuídas a mim no momento.

### Orientado a regra

- [ ] O sistema deve exibir as tarefas atribuídas ao membro da equipe na lista “Minhas Tarefas”, ordenadas por prazo de vencimento.
- [ ] Cada tarefa na lista deve exibir informações relevantes, como o nome da tarefa, o projeto associado, o prazo de vencimento e o status atual.
- [ ] O sistema deve permitir que o membro da equipe marque as tarefas como concluídas diretamente da lista “Minhas Tarefas”.

---

# User story 3
Como um gerente de projeto, quero poder atribuir tarefas a membros da equipe, para distribuir responsabilidades.

## Critérios de aceitação

### Orientado a cenário

- [ ] Dada a página de detalhes do projeto, quando eu adicionar uma nova tarefa e atribuí-la a um membro da equipe, eu espero que a tarefa seja exibida na lista de tarefas desse membro.
- [ ] Dada a página de detalhes do projeto, quando eu atribuir uma tarefa a um membro da equipe, espero que o membro da equipe receba uma notificação sobre a nova tarefa atribuída.

### Orientado a regra

- [ ] O sistema deve permitir que o gerente de projeto atribua tarefas a qualquer membro da equipe do projeto.
- [ ] O sistema deve permitir a reatribuição de tarefas entre membros da equipe.
- [ ] O sistema deve enviar uma notificação ao membro da equipe quando uma nova tarefa for atribuída a ele.
- [ ] O sistema deve atualizar a lista de tarefas do membro da equipe para refletir as tarefas recém-atribuídas.

---

# User story 4
Como um gerente de projeto, quero poder definir prioridades para as tarefas, para garantir que as atividades mais importantes sejam concluídas primeiro.

## Critérios de aceitação

### Orientado a cenário

- [ ] Dada a página de detalhes da tarefa, quando eu alterar a prioridade de uma tarefa, espero que a lista de tarefas seja atualizada para refletir a nova prioridade.
- [ ] Dada a página de detalhes da tarefa, quando eu definir uma prioridade alta para uma tarefa, espero que ela seja listada no topo da lista de tarefas.

### Orientado a regra

- [ ] O sistema deve permitir que o gerente de projeto defina a prioridade de cada tarefa individualmente.
- [ ] O sistema deve destacar visualmente as tarefas de alta prioridade na lista de tarefas.
- [ ] O sistema deve permitir a redefinição da prioridade de uma tarefa a qualquer momento.
- [ ] O sistema deve ordenar as tarefas na lista de tarefas com base em sua prioridade.

---

# User story 5
Como um gerente de projeto, quero poder exportar relatórios de progresso do projeto, para compartilhar com outras partes interessadas.

## Critérios de aceitação

### Orientado a cenário

- [ ] Dada a página do projeto, quando eu clicar em "Exportar", então eu espero que o sistema gere e faça o download do relatório.

### Orientado a regra

- [ ] O relatório exportado deve incluir informações relevantes, como progresso geral do projeto, marcos atingidos, tarefas pendentes, tarefas concluídas e membros da equipe.
- [ ] O sistema deve fornecer uma opção para incluir ou excluir detalhes específicos no relatório exportado.
- [ ] O sistema deve gerar o relatório em tempo real para garantir que as informações mais recentes sejam incluídas.

---

# User story 6
Como um gerente de projeto ou membro da equipe, quero poder adicionar notas aos projetos, para documentar informações importantes ou decisões tomadas.

## Critérios de aceitação

### Orientado a cenário

- [ ] Dada a página de detalhes do projeto, quando eu adicionar uma nova nota, então eu espero que ela seja exibida na seção de notas do projeto.
- [ ] Dada a página de detalhes do projeto, quando eu editar uma nota existente, espero que as alterações sejam salvas e refletidas na nota na seção de notas do projeto.
- [ ] Dada a página de detalhes do projeto, quando eu excluir uma nota, espero que ela seja removida da seção de notas do projeto.

### Orientado a regra

- [ ] O sistema deve permitir que o gerente de projeto e os membros da equipe adicionem, editem e excluam notas nos projetos.
- [ ] O sistema deve exibir a data e hora da última atualização da nota.
- [ ] O sistema deve permitir a formatação básica de texto nas notas.

---

# User story 7
Como um gerente de projeto ou membro da equipe, quero poder adicionar notas às tarefas, para documentar informações importantes ou decisões tomadas.

## Critérios de aceitação

### Orientado a cenário

- [ ] Dada a página de detalhes da tarefa, quando eu adicionar uma nova nota, então eu espero que ela seja exibida na seção de notas da tarefa.
- [ ] Dada a página de detalhes da tarefa, quando eu editar uma nota existente, espero que as alterações sejam salvas e refletidas na nota na seção de notas da tarefa.
- [ ] Dada a página de detalhes da tarefa, quando eu excluir uma nota, espero que ela seja removida da seção de notas da tarefa.

### Orientado a regra

- [ ] O sistema deve permitir que o gerente de projeto e os membros da equipe adicionem, editem e excluam notas nas tarefas.
- [ ] O sistema deve exibir a data e hora da última atualização da nota.
- [ ] O sistema deve permitir a formatação básica de texto nas notas.

---

# User story 8
Como um gerente de projeto, quero poder definir dependências entre tarefas, para garantir que elas sejam concluídas na ordem correta.

## Critérios de aceitação

### Orientado a cenário

- [ ] Dada a página de detalhes da tarefa, quando eu vincular uma tarefa a outra como uma dependência, então eu espero que a segunda tarefa não possa ser concluída até que a primeira seja concluída.
- [ ] Dada a página de detalhes da tarefa, quando eu remover uma dependência de uma tarefa, espero que a tarefa possa ser concluída independentemente do status da tarefa anteriormente dependente.

### Orientado a regra

- [ ] O sistema deve permitir que o gerente de projeto defina, edite e remova dependências entre tarefas.
- [ ] O sistema deve impedir que as tarefas dependentes sejam marcadas como concluídas antes das tarefas das quais elas dependem.
- [ ] O sistema deve fornecer feedback ao usuário quando uma tentativa de violar a regra de dependência for feita, por exemplo, tentar concluir uma tarefa dependente antes da tarefa da qual ela depende.
- [ ] O sistema deve exibir visualmente as dependências entre tarefas na interface do usuário.

---

# User story 9
Como um gerente de projeto, quero poder reatribuir tarefas de um membro da equipe para outro, para distribuir responsabilidades.

## Critérios de aceitação

### Orientado a cenário

- [ ] Dada a página de detalhes da tarefa, quando eu selecionar um novo membro da equipe para uma tarefa existente, espero que a tarefa seja transferida para o novo membro selecionado.
- [ ] Dada a página de detalhes da tarefa, quando eu reatribuir uma tarefa, espero que o novo membro da equipe receba uma notificação sobre a reatribuição da tarefa.

### Orientado a regra

- [ ] O sistema deve permitir que o gerente de projeto reatribua tarefas entre os membros da equipe.
- [ ] O sistema deve registrar a reatribuição no histórico da tarefa, incluindo a data e a hora da mudança.
- [ ] O sistema deve enviar uma notificação ao novo membro da equipe quando uma tarefa for reatribuída a ele.
- [ ] O sistema deve atualizar a lista de tarefas dos membros da equipe para refletir a nova atribuição.

---

# User story 10
Como um gerente de projeto, quero poder criar marcos no projeto, para monitorar o progresso em etapas críticas.

## Critérios de aceitação

### Orientado a cenário

- [ ] Dada a página de detalhes do projeto, quando eu adicionar um novo marco, espero que ele seja exibido na linha do tempo do projeto.
- [ ] Dada a página de detalhes do projeto, quando eu editar um marco existente, espero que as alterações sejam salvas e refletidas na linha do tempo do projeto.
- [ ] Dada a página de detalhes do projeto, quando eu excluir um marco, espero que ele seja removido da linha do tempo do projeto.

### Orientado a regra

- [ ] O sistema deve permitir que o gerente de projeto crie, edite e exclua marcos no projeto.
- [ ] O sistema deve exibir a data e a descrição dos marcos na linha do tempo do projeto.
- [ ] O sistema deve enviar notificações aos membros da equipe quando um marco estiver próximo de ser atingido.
- [ ] O sistema deve permitir que os marcos sejam associados a tarefas específicas no projeto.

---

# User story 11
Como um gerente de projeto, quero poder visualizar um dashboard do projeto, para ter uma visão geral rápida do status do projeto.

## Critérios de aceitação

### Orientado a cenário

- [ ] Dada a página inicial do projeto, quando eu acessar o dashboard, espero ver informações sobre o progresso do projeto, tarefas pendentes, marcos, e status geral do projeto.
- [ ] Dada a página inicial do projeto, quando houver atualizações no projeto, espero que o dashboard seja atualizado em tempo real para refletir as mudanças.

### Orientado a regra

- [ ] O sistema deve exibir um resumo das tarefas pendentes e concluídas no dashboard do projeto.
- [ ] O sistema deve exibir uma linha do tempo com os marcos e suas datas.
- [ ] O sistema deve mostrar gráficos de progresso que indicam o percentual de conclusão do projeto.
- [ ] O sistema deve permitir a personalização do dashboard para que o gerente de projeto possa escolher quais widgets exibir.

---

# User story 12
Como um gerente de projeto, quero poder arquivar projetos concluídos, para manter o sistema organizado.

## Critérios de aceitação

### Orientado a cenário

- [ ] Dada a página de detalhes do projeto, quando eu arquivar um projeto concluído, espero que ele seja removido da lista de projetos ativos.
- [ ] Dada a página de detalhes do projeto, quando eu arquivar um projeto concluído, espero que ele seja movido para uma seção de projetos arquivados acessível para consulta futura.

### Orientado a regra

- [ ] O sistema deve permitir que o gerente de projeto arquive projetos concluídos.
- [ ] O sistema deve mover projetos arquivados para uma seção separada da lista de projetos ativos.
- [ ] O sistema deve manter todas as informações do projeto arquivado para consulta futura.
- [ ] O sistema deve permitir a reativação de projetos arquivados, se necessário.

---

# User story 13
Como um membro da equipe, quero poder reportar problemas nas tarefas, para que possam ser resolvidos rapidamente.

## Critérios de aceitação

### Orientado a cenário

- [ ] Dada a página de detalhes da tarefa, quando eu reportar um problema, espero que ele seja adicionado à lista de problemas da tarefa.
- [ ] Dada a página de detalhes da tarefa, quando eu reportar um problema, espero que o gerente de projeto receba uma notificação sobre o problema.

### Orientado a regra

- [ ] O sistema deve permitir que os membros da equipe reportem problemas nas tarefas.
- [ ] O sistema deve adicionar os problemas reportados à lista de problemas da tarefa.
- [ ] O sistema deve enviar uma notificação ao gerente de projeto quando um problema for reportado.
- [ ] O sistema deve permitir a atualização e resolução de problemas na lista de problemas da tarefa.

---

# User story 14
Como um gerente de projeto, quero poder visualizar o histórico de alterações de uma tarefa, para acompanhar as mudanças feitas.

## Critérios de aceitação

### Orientado a cenário

- [ ] Dada a página de detalhes da tarefa, quando eu visualizar o histórico de alterações, espero ver uma lista de todas as alterações feitas na tarefa, incluindo data, hora e descrição das mudanças.

### Orientado a regra

- [ ] O sistema deve registrar todas as alterações feitas nas tarefas, incluindo criação, edição e reatribuição.
- [ ] O sistema deve exibir o histórico de alterações na página de detalhes da tarefa.
- [ ] O sistema deve incluir informações sobre quem fez a alteração, quando foi feita e qual foi a alteração.
- [ ] O sistema deve permitir a filtragem e ordenação do histórico de alterações para facilitar a consulta.

---

# User story 15
Como um gerente de projeto, quero poder definir permissões de acesso para membros da equipe, para controlar o que cada membro pode fazer no projeto.

## Critérios de aceitação

### Orientado a cenário

- [ ] Dada a página de configuração do projeto, quando eu definir permissões para um membro da equipe, espero que essas permissões sejam aplicadas imediatamente.
- [ ] Dada a página de configuração do projeto, quando eu remover permissões de um membro da equipe, espero que o acesso dele seja restringido conforme configurado.

### Orientado a regra

- [ ] O sistema deve permitir que o gerente de projeto defina permissões de acesso específicas para cada membro da equipe.
- [ ] O sistema deve aplicar as permissões imediatamente após serem definidas.
- [ ] O sistema deve permitir diferentes níveis de acesso, como visualização, edição e administração.
- [ ] O sistema deve registrar as alterações de permissões no histórico do projeto.

---

# User story 16
Como um gerente de projeto, quero poder configurar notificações, para receber alertas sobre eventos importantes no projeto.

## Critérios de aceitação

### Orientado a cenário

- [ ] Dada a página de configuração do projeto, quando eu configurar notificações, espero receber alertas de acordo com as minhas preferências.
- [ ] Dada a página de configuração do projeto, quando um evento importante ocorrer, espero receber uma notificação conforme configurado.

### Orientado a regra

- [ ] O sistema deve permitir que o gerente de projeto configure notificações para diferentes tipos de eventos, como conclusão de tarefas, mudanças de status, e novos comentários.
- [ ] O sistema deve enviar notificações por e-mail, SMS ou dentro do sistema, conforme configurado.
- [ ] O sistema deve permitir a personalização da frequência e do tipo de notificações recebidas.
- [ ] O sistema deve registrar o envio de notificações no histórico do projeto.

---

# User story 17
Como um gerente de projeto, quero poder acompanhar o tempo gasto nas tarefas, para monitorar a eficiência da equipe.

## Critérios de aceitação

### Orientado a cenário

- [ ] Dada a página de detalhes da tarefa, quando eu iniciar um temporizador, espero que o sistema registre o tempo gasto na tarefa.
- [ ] Dada a página de detalhes da tarefa, quando eu parar o temporizador, espero que o tempo total gasto seja atualizado.

### Orientado a regra

- [ ] O sistema deve permitir que os membros da equipe iniciem e parem um temporizador para registrar o tempo gasto nas tarefas.
- [ ] O sistema deve registrar o tempo total gasto em cada tarefa e exibir essa informação na página de detalhes da tarefa.
- [ ] O sistema deve gerar relatórios de tempo gasto para cada tarefa, membro da equipe e projeto.
- [ ] O sistema deve permitir a edição manual do tempo registrado para correções, se necessário.

---

# User story 18
Como um gerente de projeto, quero poder integrar o sistema com ferramentas de terceiros, para melhorar a colaboração e a produtividade.

## Critérios de aceitação

### Orientado a cenário

- [ ] Dada a página de integração, quando eu configurar uma nova integração, espero que o sistema conecte e sincronize os dados com a ferramenta de terceiros.
- [ ] Dada a página de integração, quando eu remover uma integração, espero que a conexão com a ferramenta de terceiros seja desfeita.

### Orientado a regra

- [ ] O sistema deve permitir a integração com ferramentas de terceiros, como Trello, Slack, e Google Drive.
- [ ] O sistema deve sincronizar dados como tarefas, documentos e notificações entre o sistema e a ferramenta de terceiros.
- [ ] O sistema deve permitir a configuração e remoção de integrações de forma fácil e intuitiva.
- [ ] O sistema deve garantir a segurança e a privacidade dos dados durante a sincronização com ferramentas de terceiros.

---

# User story 19
Como um gerente de projeto, quero poder visualizar relatórios de desempenho da equipe, para identificar áreas de melhoria.

## Critérios de aceitação

### Orientado a cenário

- [ ] Dada a página de relatórios, quando eu gerar um relatório de desempenho da equipe, espero ver métricas como tarefas concluídas, tempo gasto e eficiência.

### Orientado a regra

- [ ] O sistema deve gerar relatórios detalhados de desempenho da equipe, incluindo métricas de tarefas concluídas, tempo gasto e eficiência.
- [ ] O sistema deve permitir a personalização dos relatórios, incluindo filtros por membro da equipe, período e tipo de tarefa.
- [ ] O sistema deve exibir os relatórios em um formato visualmente intuitivo, como gráficos e tabelas.
- [ ] O sistema deve permitir a exportação dos relatórios em formatos comuns, como PDF e Excel.

---

# User story 20
Como um membro da equipe, quero  poder visualizar os marcos do projeto na barra de progresso, para entender visualmente o planejamento de conclusão do projeto em relação às datas determinadas

## Critérios de aceitação

### Orientado a cenário

- [ ] Dada a página de visão geral do projeto, quando eu visualizar a barra de progresso, espero que os marcos planejados sejam exibidos como pontos de referência ao longo da linha de tempo.
- [ ] Dada a página de visão geral do projeto, quando um marco for alcançado, espero que a barra de progresso seja atualizada automaticamente para refletir a conclusão do marco.

### Orientado a regra

- [ ] O sistema deve exibir visualmente os marcos do projeto na barra de progresso como pontos de referência ao longo da linha de tempo.
- [ ] O sistema deve atualizar a barra de progresso em tempo real à medida que os marcos são alcançados.

---

# User story 21
Como um gerente de projeto, quero  poder visualizar o status do projeto, para  monitorar o progresso do projeto e tomar decisões informadas

## Critérios de aceitação

### Orientado a cenário

- [ ] Dada a página de visão geral do projeto, quando eu visualizar a seção de status do projeto, então eu espero ver o status atual do projeto (adiantado, atrasado, em dia) com base nos marcos definidos e concluídos.
- [ ] Dada a página de visão geral do projeto, quando um marco for concluído antes da data prevista, então eu espero que o status do projeto seja atualizado para “adiantado”.
- [ ] Dada a página de visão geral do projeto, quando um marco não for concluído até a data prevista, então eu espero que o status do projeto seja atualizado para “atrasado”.

### Orientado a regra

- [ ] O sistema deve calcular o status do projeto com base na comparação entre as datas de conclusão previstas e reais dos marcos.
- [ ] O sistema deve atualizar automaticamente o status do projeto à medida que os marcos são concluídos.
- [ ] O sistema deve permitir que o gerente de projeto visualize o status do projeto a qualquer momento na página de visão geral do projeto.
- [ ] O sistema deve fornecer uma representação visual do status do projeto, como uma barra de cores ou um ícone, para facilitar a compreensão rápida do status.

---

# User story 22
Como um gerente de projeto, quero  poder confrontar os pontos atribuídos a cada tarefa com o tempo real gasto, para avaliar a precisão das estimativas e melhorar o planejamento futuro.

## Critérios de aceitação

### Orientado a cenário

- [ ] Dada a página de detalhes da tarefa, quando eu visualizar os detalhes de uma tarefa, então eu espero ver tanto os pontos atribuídos quanto o tempo real registrado para a tarefa.
- [ ] Dada a página de relatórios do projeto, quando eu gerar um relatório de produtividade, então eu espero ver uma comparação entre os pontos atribuídos e o tempo real gasto em cada tarefa.


### Orientado a regra

- [ ] O sistema deve registrar e exibir o tempo real gasto em cada tarefa.
- [ ] O sistema deve permitir que o gerente de projeto compare o tempo real gasto com os pontos atribuídos para cada tarefa.
- [ ] O sistema deve fornecer relatórios que mostrem a comparação entre os pontos atribuídos e o tempo real gasto em cada tarefa.

---

# User story 23
Como: um gerente de projeto, quero  poder atribuir setores aos usuários e aos projetos, para facilitar a atribuição de membros aos projetos com base em suas áreas de especialização.

## Critérios de aceitação

### Orientado a cenário

- [ ] Dada a página de detalhes do usuário, quando eu selecionar um setor para o usuário e salvar as alterações, então eu espero que o setor do usuário seja atualizado para refletir a nova atribuição.
- [ ] Dada a página de detalhes do projeto, quando eu selecionar um setor para o projeto e salvar as alterações, então eu espero que o setor do projeto seja atualizado para refletir a nova atribuição.
- [ ] Dada a página de criação de novo projeto, quando eu estiver atribuindo membros à equipe do projeto, espero poder filtrar os usuários com base em seus setores.

### Orientado a regra

- [ ] O sistema deve permitir que o gerente de projeto atribua setores aos usuários.
- [ ] O sistema deve permitir que o gerente de projeto atribua setores aos projetos.
- [ ] O sistema deve permitir que o gerente de projeto filtre usuários por setor ao atribuir membros à equipe do projeto.

---

# User story 24
Como um gerente de projeto, quero poder visualizar uma tela de relatório financeiro que liste todos os projetos e mostre a relação de preço x horas trabalhadas, para ter uma visão geral do desempenho financeiro de todos os projetos, entender a eficiência do trabalho em termos de custo e otimizar a precificação dos serviços

## Critérios de aceitação

### Orientado a cenário

- [ ] Dada a página de relatórios financeiros, quando eu acessá-la, então eu espero ver uma lista de todos os projetos com a relação de preço x horas trabalhadas para cada um.
- [ ] Dada a página de relatórios financeiros, quando eu selecionar um projeto específico, espero ver um detalhamento mais profundo da relação de preço x horas trabalhadas para esse projeto.


### Orientado a regra

- [ ] O sistema deve permitir que o gerente de projeto visualize uma tela de relatório financeiro com todos os projetos listados, apresentando os dados de forma visualmente intuitiva.
- [ ] O sistema deve calcular e exibir a relação de preço x horas trabalhadas para cada projeto na lista.
- [ ] O sistema deve permitir que o gerente de projeto selecione um projeto específico para ver um detalhamento mais profundo da relação de preço x horas trabalhadas.


---
