## Volglass
Volglass is a fork of [MindStone(digital-garden)](https://github.com/TuanManhCao/digital-garden)

It is this project's goal to assist publishing your Obsidian contents.

Volglass documentation is build my self!!

https://volglass.turtton.net

# Features

- **Search** documents
- **Dark** Theme
- **Embed Link** Support
- **Mermaid** Support
- **TeX** Support(partial)
- **Canvas** Support

Also, I fixed many issues that the original has.

## Getting started

See [Getting Started](https://volglass.turtton.net/docs/learn/Getting+Started)

## For developers
Instead of cloning this repository, I recommend to use [volglass-cli](https://github.com/turtton/volglass-cli).

1. `npm init --yes`
2. `npm install --save-dev volglass-cli`
3. `npx volglass init`
4. `npx volglass dev --use-ssh -T`

Now, volglass-cli does these tasks
- clone this repository(with ssh`--use-ssh`) to dev directory
- download template page contents(`-T`)
- run `next dev`

Please run `npx volglass dev` after the second time.
