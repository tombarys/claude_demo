#!/bin/bash

echo "🚀 Spouštím nREPL server..."
echo ""
echo "Server poběží na náhodném portu."
echo "Port najdeš v souboru .nrepl-port"
echo ""
echo "Pro ukončení stiskni Ctrl+C"
echo ""
echo "═══════════════════════════════════════"
echo ""

clj -M -m nrepl.cmdline
