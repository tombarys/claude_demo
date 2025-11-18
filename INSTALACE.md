# Instalační návod pro clojure-mcp-light

Tento návod popisuje kompletní instalaci `clojure-mcp-light` a `Claude Code` na macOS.

## Předpoklady

Před instalací ověř, že máš nainstalované:

```bash
# Ověř instalace
which bb          # Babashka
which bbin        # bbin (Babashka package manager)
which clj         # Clojure CLI
which npm         # Node.js / npm
```

Pokud něco chybí, nainstaluj:
- **Babashka**: `brew install babashka/brew/babashka`
- **bbin**: `brew install babashka/brew/bbin`
- **Clojure**: `brew install clojure/tools/clojure`
- **Node.js**: `brew install node`

---

## Krok 1: Nainstaluj Rust a Cargo

Rust je potřeba pro build `parinfer-rust`:

```bash
# Nainstaluj Rust
curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh -s -- -y

# Aktivuj Rust v aktuálním shellu
source $HOME/.cargo/env
```

---

## Krok 2: Nainstaluj parinfer-rust

```bash
# Klonuj repozitář
cd ~/Dev  # nebo jiný adresář dle potřeby
git clone https://github.com/eraserhd/parinfer-rust.git
cd parinfer-rust

# Build a instalace
cargo build --release
cargo install --path .

# Ověř instalaci
which parinfer-rust
# Mělo by vrátit: /Users/<username>/.cargo/bin/parinfer-rust

# Vyčisti dočasné soubory
cd ..
rm -rf parinfer-rust
```

---

## Krok 3: Nainstaluj clojure-mcp-light

```bash
# Klonuj repozitář
cd ~/Dev  # nebo jiný adresář
git clone https://github.com/bhauman/clojure-mcp-light.git
cd clojure-mcp-light

# Vyčisti případně poškozené Clojure tools
rm -rf ~/.deps.clj

# Nainstaluj oba příkazy pomocí bbin
bbin install .
bbin install . --as clj-nrepl-eval --main-opts '["-m" "clojure-mcp-light.nrepl-eval"]'

# Ověř instalaci
which clj-paren-repair-claude-hook
# Mělo by vrátit: /Users/<username>/.local/bin/clj-paren-repair-claude-hook

which clj-nrepl-eval
# Mělo by vrátit: /Users/<username>/.local/bin/clj-nrepl-eval
```

---

## Krok 4: Nainstaluj cljfmt (volitelné, ale doporučené)

```bash
bbin install io.github.weavejester/cljfmt

# Ověř instalaci
which cljfmt
# Mělo by vrátit: /Users/<username>/.local/bin/cljfmt
```

---

## Krok 5: Nainstaluj Claude Code

```bash
# Instalace přes npm
npm install -g @anthropic-ai/claude-code

# Ověř instalaci
which claude
claude --version
# Mělo by vrátit: 2.0.44 (Claude Code) nebo novější
```

---

## Krok 6: Nakonfiguruj Claude Code hooks

Vytvoř konfigurační soubor pro Claude Code:

```bash
# Vytvoř adresář, pokud neexistuje
mkdir -p ~/.claude

# Vytvoř konfigurační soubor
cat > ~/.claude/settings.local.json << 'EOF'
{
  "hooks": {
    "PreToolUse": [
      {
        "matcher": "Write|Edit",
        "hooks": [
          {
            "type": "command",
            "command": "clj-paren-repair-claude-hook --cljfmt"
          }
        ]
      }
    ],
    "PostToolUse": [
      {
        "matcher": "Edit|Write",
        "hooks": [
          {
            "type": "command",
            "command": "clj-paren-repair-claude-hook --cljfmt"
          }
        ]
      }
    ],
    "SessionEnd": [
      {
        "hooks": [
          {
            "type": "command",
            "command": "clj-paren-repair-claude-hook"
          }
        ]
      }
    ]
  }
}
EOF

# Ověř, že soubor byl vytvořen
cat ~/.claude/settings.local.json
```

---

## Krok 7: Přidej aliasy do shell RC souboru

Pro pohodlnější práci přidej aliasy do `~/.zshrc` (nebo `~/.bashrc`):

```bash
# Přidej na konec souboru
cat >> ~/.zshrc << 'EOF'

# Clojure REPL aliases
alias repl-connect='clj -Sdeps "{:deps {nrepl/nrepl {:mvn/version \"1.0.0\"}}}" -M -m nrepl.cmdline --connect --host localhost --port'
EOF

# Načti změny
source ~/.zshrc
```

---

## Krok 8: Ověř PATH

Ujisti se, že všechny nástroje jsou v PATH:

```bash
# Přidej do ~/.zshrc (pokud tam ještě nejsou)
cat >> ~/.zshrc << 'EOF'

# Přidej cesty pro Rust a lokální binaries
export PATH="$HOME/.cargo/bin:$HOME/.local/bin:$PATH"
EOF

# Načti změny
source ~/.zshrc
```

---

## Ověření instalace

Zkontroluj, že všechny nástroje fungují:

```bash
# Test 1: Claude Code
claude --version
# Očekáváno: 2.0.44 (Claude Code) nebo novější

# Test 2: clj-paren-repair-claude-hook
clj-paren-repair-claude-hook --help
# Očekáváno: Zobrazí help message

# Test 3: clj-nrepl-eval
clj-nrepl-eval --help
# Očekáváno: Zobrazí help message

# Test 4: parinfer-rust
which parinfer-rust
# Očekáváno: /Users/<username>/.cargo/bin/parinfer-rust

# Test 5: cljfmt
which cljfmt
# Očekáváno: /Users/<username>/.local/bin/cljfmt

# Test 6: Konfigurace
cat ~/.claude/settings.local.json
# Očekáváno: JSON konfigurace s hooks
```

---

## Vytvoření testovacího projektu (volitelné)

Pro otestování můžeš vytvořit jednoduchý testovací projekt:

```bash
# Vytvoř projekt
cd ~/Dev
mkdir claude-test
cd claude-test

# Vytvoř deps.edn
cat > deps.edn << 'EOF'
{:deps {org.clojure/clojure {:mvn/version "1.12.0"}
        nrepl/nrepl {:mvn/version "1.0.0"}}
 :paths ["src"]}
EOF

# Vytvoř zdrojový soubor
mkdir -p src/demo
cat > src/demo/core.clj << 'EOF'
(ns demo.core)

(defn hello [name]
  (str "Hello, " name "!"))

(defn add [a b]
  (+ a b))
EOF

# Vytvoř helper skripty
cat > start-repl.sh << 'EOF'
#!/bin/bash
echo "🚀 Spouštím nREPL server..."
echo "Server poběží na náhodném portu."
echo "Port najdeš v souboru .nrepl-port"
echo "Pro ukončení stiskni Ctrl+C"
echo "═══════════════════════════════════════"
clj -M -m nrepl.cmdline
EOF

chmod +x start-repl.sh

cat > test-connection.sh << 'EOF'
#!/bin/bash
if [ ! -f .nrepl-port ]; then
    echo "❌ Soubor .nrepl-port nenalezen!"
    echo "   Nejprve spusť nREPL server: ./start-repl.sh"
    exit 1
fi

PORT=$(cat .nrepl-port)
echo "🔍 Testuji připojení k nREPL na portu $PORT..."
echo ""

echo "Test 1: Základní výpočet"
clj-nrepl-eval -p $PORT '(+ 1 2 3)'
echo ""

echo "Test 2: Načtení namespace"
clj-nrepl-eval -p $PORT "(require '[demo.core :as demo])"
echo ""

echo "Test 3: Volání funkce hello"
clj-nrepl-eval -p $PORT '(demo/hello "Test")'
echo ""

echo "Test 4: Volání funkce add"
clj-nrepl-eval -p $PORT '(demo/add 10 20)'
echo ""

echo "✅ Všechny testy dokončeny!"
EOF

chmod +x test-connection.sh

echo "✅ Testovací projekt vytvořen v $(pwd)"
```

---

## Test funkcionality

Otestuj celý setup:

```bash
# Terminál 1: Spusť nREPL
cd ~/Dev/claude-test
./start-repl.sh
# Poznamenej si port (např. 50706)

# Terminál 2: Otestuj spojení
cd ~/Dev/claude-test
./test-connection.sh
# Měly by proběhnout všechny testy úspěšně

# Terminál 3: Spusť Claude
cd ~/Dev/claude-test
claude
# V Claude zkus: "Zobraz obsah src/demo/core.clj"
```

---

## Řešení problémů

### Problém: "command not found" pro clj-paren-repair-claude-hook

```bash
# Přidej do PATH
echo 'export PATH="$HOME/.local/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

### Problém: "command not found" pro parinfer-rust

```bash
# Přidej do PATH
echo 'export PATH="$HOME/.cargo/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

### Problém: bbin selže s "Clojure tools not yet in expected location"

```bash
# Vyčisti a zkus znovu
rm -rf ~/.deps.clj
bbin install <command>
```

### Problém: Claude nenajde hooks

```bash
# Ověř konfiguraci
cat ~/.claude/settings.local.json

# Ověř, že jsou příkazy dostupné
which clj-paren-repair-claude-hook
which parinfer-rust
```

---

## Další kroky

Po úspěšné instalaci:

1. Přečti si `/Users/<username>/Dev/clojure-mcp-light/NAVOD_CZ.md` pro detailní dokumentaci
2. Experimentuj s testovacím projektem
3. Použij Claude v reálných Clojure projektech

---

## Reference

- **clojure-mcp-light**: https://github.com/bhauman/clojure-mcp-light
- **parinfer-rust**: https://github.com/eraserhd/parinfer-rust
- **Claude Code**: https://www.anthropic.com/claude
- **nREPL**: https://nrepl.org/

---

**Vytvořeno:** 18. listopadu 2024  
**Verze:** 1.0  
**Testováno na:** macOS (Apple Silicon)
