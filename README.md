[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/Q-troXqB)

# Shooter game

A dynamic 2D two-(or can be more)-player shooter built with JavaFX.

## 🎮 Game Description

**"Shooter game"** is a fast-paced 2D shooter for two players with a fixed camera. Each player’s character continuously rotates. When a control button is pressed, the character starts moving in the current direction and shoots.

The map includes:
- Static objects that block movement
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

## 🛠️ Current Progress

✅ Game architecture fully implemented  
✅ Collision system working  
✅ Player 1 movement via 'W' key  
✅ Map fully configurable through JSON files  
✅ Some textures added  
✅ Dynamicly scalable tiles, hitboxes

---

## 🚧 TODO (High Priority)

- [ ] Implement MVP pattern for the UI
- [ ] Add a main menu
- [ ] Create a `SceneManager` for switching between scenes
- [ ] Implement proper key handling for both players
- [ ] Enable player shooting functionality

---

## 📲 How to run?
**class GUIStarter** is an entrypoint of the app

---

## 📦 Tech Stack

- JavaFX
- Maven
- Jackson(for configs)

## GIF

![GIF](https://i.imgur.com/JeUFA6A.gif)
Lagging because a little bit compressed


