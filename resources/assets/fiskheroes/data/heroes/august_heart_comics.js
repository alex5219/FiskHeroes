function init(hero) {
    hero.setName("hero.fiskheroes.august_heart_comics.name");
    hero.setVersion("item.superhero_armor.version.comics");
    hero.setTier(2);
    
    hero.setHelmet("item.superhero_armor.piece.goggles");
    hero.setChestplate("item.superhero_armor.piece.jacket");
    hero.setLeggings("item.superhero_armor.piece.pants");
    hero.setBoots("item.superhero_armor.piece.boots");
    hero.addEquipment("fiskheroes:flash_ring");
    
    hero.addAbilities("fiskheroes:super_speed", "fiskheroes:enhanced_reflexes", "fiskheroes:cellular_regeneration", "fiskheroes:accelerated_perception");
    hero.addAttribute("ATTACK_DAMAGE", 0.1, 1);
    hero.addAttribute("BASE_SPEED_LEVELS", 4.0, 0);
    hero.addAttribute("FALL_RESISTANCE", 5.0, 0);
    hero.addAttribute("JUMP_HEIGHT", 1.0, 0);
    hero.addAttribute("PUNCH_DAMAGE", 3.0, 0);
    
    hero.addKeyBind("SUPER_SPEED", "key.superSpeed", 1);
    hero.addKeyBind("SLOW_MOTION", "key.slowMotion", 2);
    hero.addKeyBind("DECELERATE", "key.decelerate", 3);
    hero.addKeyBind("ACCELERATE", "key.accelerate", 4);
}
