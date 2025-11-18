# Claude Code Test Project

Tento projekt slouží k testování Claude Code s clojure-mcp-light.

## Jak začít

### 1. Spusť nREPL server

V tomto adresáři spusť:

```bash
clj -M -m nrepl.cmdline
```

Server vypíše port (nebo ho najdeš v `.nrepl-port`).

### 2. Testuj nREPL spojení

V novém terminálu:

```bash
# Zjisti port
cat .nrepl-port

# Vyhodnoť kód (nahraď 7888 skutečným portem)
clj-nrepl-eval -p 7888 "(+ 1 2 3)"

# Načti namespace
clj-nrepl-eval -p 7888 "(require '[demo.core :as demo])"

# Zavolej funkci
clj-nrepl-eval -p 7888 "(demo/hello \"Claude\")"
```

### 3. Použij Claude Code

V novém terminálu v tomto adresáři:

```bash
claude
```

Pak zkus příkazy jako:

- "Zobraz obsah souboru src/demo/core.clj"
- "Přidej novou funkci multiply do namespace demo.core"
- "Uprav funkci hello, aby vracela UPPERCASE text"

Claude automaticky opraví závorky díky nakonfigurovaným hooks!

### 4. Testuj automatickou opravu závorek

Zkus to Claude říct:

> "Vytvoř novou funkci broken v src/demo/core.clj s chybnou závorkou"

Hooks by měly automaticky opravit syntax!

## Strukturtura projektu

```
claude-test/
├── deps.edn          # Clojure dependencies
├── src/
│   └── demo/
│       └── core.clj  # Hlavní namespace
└── README.md
```
