[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/Q-troXqB)

Full Documentation you can find in the folder `/docs/`

# Multiplayer Shooter Minigame

A dynamic 2D two-(or can be more)-player shooter built with JavaFX.

## 🎮 Game Description

**"Multiplayer Shooter Minigame"** is a fast-paced 2D shooter for two players with a fixed camera. Each player’s character continuously rotates. When a control button is pressed, the character starts moving in the current direction and shoots.

The map includes:
- Static objects that block movement
- Breakable blocks
- Passable covers
- Various power-ups

### Power-ups provide temporary bonuses:

- Shield
- Movement speed boost
- Firing rate increase
- Damage boost

Gameplay features:
- Two weapon modes: unlimited ammo and limited (regenerating) ammo
- Players usually die after two hits (turn red after the first)
- Power-ups visually affect bullets or the player’s appearance
- Respawning at a designated point with temporary invulnerability
- Timed matches where the winner is the player with the most kills

---

## 📲 How to run?
**class GUIStarter** is an entrypoint of the app

---

## 📦 Tech Stack

- JavaFX
- Maven
- Jackson(for configs)
- slf4j(logs)
- log4j(logs)
- hibernate-validator(validation)
- JUnit(tests)
- Mockito(tests)

## GIF

![GIF](https://i.imgur.com/xa7a79W.gif)
Lagging because a little bit compressed


