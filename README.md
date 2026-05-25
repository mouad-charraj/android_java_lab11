# Lab 11 - Géolocalisation - Projet MouadApplication Android de cartographie permettant de suivre la position GPS en temps réel.

## Transition vers OpenStreetMap (OSM)
Initialement, ce lab devait utiliser Google Maps. Cependant, suite aux nouvelles contraintes de Google Cloud (obligation d'activer la facturation et de fournir une carte bancaire pour obtenir une clé API fonctionnelle), j'ai choisi d'utiliser **OpenStreetMap (osmdroid)**.

Ce choix permet de :
- Garantir un fonctionnement gratuit et immédiat pour le correcteur.
- Éviter les filigranes "Development purposes only" qui masquent la carte.
- Valider 100% des objectifs techniques : gestion du GPS, permissions runtime, marqueurs dynamiques et interactions tactiles.

## Fonctionnalités personnalisées
- **Suivi GPS** : Localisation de Mouad en temps réel sur la carte.
- **Marqueur Mouad** : Utilisation d'une icône spécifique pour ma position.
- **Interactivité** : Ajout de marqueurs de destination par simple clic.
- **Bouton Recentrer** : Bouton dédié pour revenir instantanément sur ma position.
- **Nettoyage** : Appui long sur la carte pour effacer tous les points ajoutés.

## Démonstration Vidéo
https://github.com/user-attachments/assets/be518a91-24a5-420a-a73c-a3ddf3f219be

## Installation
1. Cloner ce dépôt.
2. Ouvrir le projet dans Android Studio.
3. Compiler et lancer sur un smartphone ou émulateur.
4. Accepter la demande de permission de localisation au démarrage.





