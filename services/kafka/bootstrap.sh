#!/bin/bash
set -e

echo "[+] Building Apptainer image..."
apptainer build kafka.sif kafka.def
echo "[✓] Build complete: kafka.sif"
