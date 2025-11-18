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
echo "Příkaz: (+ 1 2 3)"
clj-nrepl-eval -p $PORT "(+ 1 2 3)"
echo ""

echo "Test 2: Načtení namespace"
echo "Příkaz: (require '[demo.core :as demo])"
clj-nrepl-eval -p $PORT "(require '[demo.core :as demo])"
echo ""

echo "Test 3: Volání funkce"
echo "Příkaz: (demo/hello \"Claude\")"
clj-nrepl-eval -p $PORT "(demo/hello \"Claude\")"
echo ""

echo "Test 4: Volání funkce add"
echo "Příkaz: (demo/add 10 20)"
clj-nrepl-eval -p $PORT "(demo/add 10 20)"
echo ""

echo "✅ Všechny testy dokončeny!"
