{
  description = "A basic flake with a shell";
  inputs.nixpkgs.url = "github:NixOS/nixpkgs/nixpkgs-unstable";
  inputs.flake-utils.url = "github:numtide/flake-utils";

  outputs = { nixpkgs, flake-utils, ... }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
        corepack = with pkgs; stdenv.mkDerivation {
            name = "corepack";
            buildInputs = [ pkgs.nodejs-slim ];
            phases = [ "installPhase" ];
              installPhase = ''
                  mkdir -p $out/bin
                  corepack enable --install-directory=$out/bin
              '';
        };
      in
      {
        devShells.default = with pkgs; mkShell {
          packages = [ bashInteractive corepack ];
        };
      });
}
