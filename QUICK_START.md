# Quick Start Guide - Claude Code s clojure-mcp-light

## 🚀 Rychlý start (5 minut)

### Terminál 1: Spusť nREPL

```bash
cd /Users/tomas/Dev/claude-test
clj -M -m nrepl.cmdline
```

Počkej, až se zobrazí: `nREPL server started on port XXXX...`

---

### Terminál 2: Testuj spojení

```bash
cd /Users/tomas/Dev/claude-test

# Zjisti port
PORT=$(cat .nrepl-port)
echo "nREPL běží na portu: $PORT"

# Testuj základní výpočet
clj-nrepl-eval -p $PORT "(+ 1 2 3)"
# Mělo by vrátit: 6

# Načti náš namespace
clj-nrepl-eval -p $PORT "(require '[demo.core :as demo])"

# Zavolej funkci
clj-nrepl-eval -p $PORT "(demo/hello \"Claude\")"
# Mělo by vrátit: "Hello, Claude!"
```

Pokud vše funguje, jdi na další krok! ✅

---

### Terminál 3: Spusť Claude Code

```bash
cd /Users/tomas/Dev/claude-test
claude
```

---

## 💬 Příklady příkazů pro Claude

Zkus tyto příkazy v Claude (stačí zkopírovat a poslat):

### 1. Čtení kódu
```
Zobraz obsah souboru src/demo/core.clj
```

### 2. Přidání nové funkce
```
Přidej do src/demo/core.clj novou funkci multiply, která vynásobí dvě čísla
```

### 3. Úprava existující funkce
```
Uprav funkci hello v src/demo/core.clj tak, aby vracela text velkými písmeny
```

### 4. Test automatické opravy závorek
```
Vytvoř funkci test-fn v src/demo/core.clj, která sečte (+ 10 20)
```

Hooks by měly automaticky zajistit správné závorky!

---

## 🔍 Ověření, že hooks fungují

Po každé editaci zkontroluj soubor `.clojure-mcp-light-hooks.log`:

```bash
cat .clojure-mcp-light-hooks.log
```

Měl bys vidět záznamy o kontrolách závorek.

---

## 🧪 Test REPL integrace

Po úpravách můžeš kód okamžitě testovat:

```bash
# Reload namespace
clj-nrepl-eval -p $(cat .nrepl-port) "(require '[demo.core :as demo] :reload)"

# Testuj novou/upravenou funkci
clj-nrepl-eval -p $(cat .nrepl-port) "(demo/multiply 6 7)"
```

---

## ⚙️ Nastavení

Hooks jsou nakonfigurovány v: `~/.claude/settings.local.json`

```bash
cat ~/.claude/settings.local.json
```

---

## 🛠️ Řešení problémů

### Claude nenajde příkazy

Zkontroluj PATH:
```bash
which clj-paren-repair-claude-hook
which clj-nrepl-eval
which parinfer-rust
```

Pokud vrací prázdno, přidej do `~/.zshrc` nebo `~/.bashrc`:
```bash
export PATH="$HOME/.local/bin:$HOME/.cargo/bin:$PATH"
```

### nREPL nefunguje

```bash
# Zkontroluj, zda server běží
lsof -i :$(cat .nrepl-port)

# Restartuj server
# Ctrl+C v terminálu 1, pak znovu spusť:
clj -M -m nrepl.cmdline
```

### Závorky se neopravují

```bash
# Zkontroluj, že parinfer-rust funguje
echo "(+ 1 2" | parinfer-rust --mode smart

# Zkontroluj logs
cat .clojure-mcp-light-hooks.log
```

---

## 📚 Další kroky

1. Zkus vytvořit složitější funkce s vnořenými závorkami
2. Zkus více namespaces
3. Experimentuj s různými Claude příkazy
4. Podívej se na `/Users/tomas/Dev/clojure-mcp-light/NAVOD_CZ.md` pro detailní dokumentaci

---

## ✨ Pro tip

Můžeš kombinovat Claude s manuálním REPL testováním:

1. Claude vytvoří/upraví funkci
2. Okamžitě ji otestuješ přes `clj-nrepl-eval`
3. Podle výsledků řekneš Claude, co upravit
4. Opakuj!

Šťastné kódování! 🎉
