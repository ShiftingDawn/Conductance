package conductance.init.recipe;

import conductance.api.CAPI;
import conductance.api.NCItems;
import conductance.api.NCMaterialFlags;
import conductance.api.NCMaterialTraits;
import conductance.api.NCRecipeTypes;
import conductance.api.material.Material;
import conductance.api.material.MaterialTraitBlast;
import conductance.api.recipe.RecipeDataTokens;
import conductance.api.recipe.event.RegisterRecipeEvent;
import conductance.api.util.ExtruderShape;
import conductance.lib.pipenet.WireType;
import static conductance.api.CAPI.TAG_HAMMERS;
import static conductance.api.CAPI.TAG_WIRE_CUTTERS;
import static conductance.api.CAPI.materials;
import static conductance.api.NCMaterialGenerationHandlers.BOLT;
import static conductance.api.NCMaterialGenerationHandlers.DUST;
import static conductance.api.NCMaterialGenerationHandlers.FINE_WIRE;
import static conductance.api.NCMaterialGenerationHandlers.FOIL;
import static conductance.api.NCMaterialGenerationHandlers.FRAME_BOX;
import static conductance.api.NCMaterialGenerationHandlers.GEAR;
import static conductance.api.NCMaterialGenerationHandlers.GEAR_SMALL;
import static conductance.api.NCMaterialGenerationHandlers.GEM;
import static conductance.api.NCMaterialGenerationHandlers.GEM_EXQUISITE;
import static conductance.api.NCMaterialGenerationHandlers.GEM_FLAWLESS;
import static conductance.api.NCMaterialGenerationHandlers.INGOT;
import static conductance.api.NCMaterialGenerationHandlers.NUGGET;
import static conductance.api.NCMaterialGenerationHandlers.PLATE;
import static conductance.api.NCMaterialGenerationHandlers.PLATE_DENSE;
import static conductance.api.NCMaterialGenerationHandlers.PLATE_DOUBLE;
import static conductance.api.NCMaterialGenerationHandlers.RING;
import static conductance.api.NCMaterialGenerationHandlers.ROD;
import static conductance.api.NCMaterialGenerationHandlers.ROTOR;
import static conductance.api.NCMaterialGenerationHandlers.SCREW;
import static conductance.api.NCMaterialGenerationHandlers.STORAGE_BLOCK;
import static conductance.api.NCMaterialGenerationHandlers.WIRE_12X;
import static conductance.api.NCMaterialGenerationHandlers.WIRE_16X;
import static conductance.api.NCMaterialGenerationHandlers.WIRE_1X;
import static conductance.api.NCMaterialGenerationHandlers.WIRE_2X;
import static conductance.api.NCMaterialGenerationHandlers.WIRE_4X;
import static conductance.api.NCMaterialGenerationHandlers.WIRE_8X;
import static conductance.api.NCTiers.LV;
import static conductance.api.recipe.RecipeHelper.calc;

final class MaterialRecipes {

	public static void add(final RegisterRecipeEvent event) {
		for (final Material material : CAPI.regs().materials()) {
			MaterialRecipes.addAllRecipes(event, material);
			if (material.hasTrait(NCMaterialTraits.WIRE)) {
				MaterialRecipes.addWireRecipes(event, material);
			}
			if (material.hasTrait(NCMaterialTraits.BLAST)) {
				MaterialRecipes.addBlastRecipes(event, material, material.getTrait(NCMaterialTraits.BLAST));
			}
		}
	}

	private static void addAllRecipes(final RegisterRecipeEvent event, final Material material) {
		if (STORAGE_BLOCK.test(material)) {
			if (!materials().hasItemOverride(material, STORAGE_BLOCK)) {
				if (material.hasFlag(NCMaterialFlags.INGOT)) {
					event.shapeless("%s_block".formatted(material.getName()), materials().getItem(material, STORAGE_BLOCK), b -> b
						.add(materials().getItemTag(material, INGOT), 9));
					event.shapeless("%s_ingot_from_block".formatted(material.getName()), materials().getItem(material, INGOT, 9), b -> b
						.add(materials().getItemTag(material, STORAGE_BLOCK)));
				} else if (material.hasFlag(NCMaterialFlags.GEM)) {
					event.shapeless("%s_block".formatted(material.getName()), materials().getItem(material, STORAGE_BLOCK), b -> b
						.add(materials().getItemTag(material, GEM), 9));
					event.shapeless("%s_from_block".formatted(material.getName()), materials().getItem(material, GEM, 9), b -> b
						.add(materials().getItemTag(material, STORAGE_BLOCK)));
				}
			}
		}
		//TODO smash gems into lower tier gems
		if (NUGGET.test(material)) {
			if (!materials().hasItemOverride(material, NUGGET)) {
				if (material.hasFlag(NCMaterialFlags.INGOT)) {
					event.shapeless("%s_nugget".formatted(material.getName()), materials().getItem(material, NUGGET, 9), b -> b
						.add(materials().getItemTag(material, INGOT)));
					event.shapeless("%s_ingot_from_nuggets".formatted(material.getName()), materials().getItem(material, INGOT), b -> b
						.add(materials().getItemTag(material, NUGGET), 9));
				}
			}
		}
		if (PLATE.test(material)) {
			if (INGOT.test(material)) {
				event.shaped("%s_plate".formatted(material.getName()), materials().getItem(material, PLATE), b -> b
					.pattern("H", "a", "a").key('a', materials().getItemTag(material, INGOT)));
				calc(material, INGOT, PLATE, (int) material.getMass(), LV, (inAmount, outAmount, time, energy) -> {
					event.create("%s_plate".formatted(material.getName()), NCRecipeTypes.BENDING_MACHINE, b -> b
						.in(material, INGOT, inAmount).out(material, PLATE, outAmount).program(1).duration(time).energyIn(LV));
				});
			}
			if (GEM.test(material) && STORAGE_BLOCK.test(material)) {
				calc(material, STORAGE_BLOCK, PLATE, (int) material.getMass(), LV, (inAmount, outAmount, time, energy) -> {
					event.create("%s_plate".formatted(material.getName()), NCRecipeTypes.CUTTING_MACHINE, b -> b
						.in(material, STORAGE_BLOCK, inAmount).out(material, PLATE, outAmount).duration(time).energyIn(LV));
				});
			}
			if (DUST.test(material) && !INGOT.test(material)) {
				calc(material, DUST, PLATE, (int) material.getMass(), LV, (inAmount, outAmount, time, energy) -> {
					final int realTime = GEM.test(material) ? time * 2 : time; //Block in cutting machine is the intended way for gems
					event.create("%s_plate".formatted(material.getName()), NCRecipeTypes.COMPRESSOR, b -> b
						.in(material, DUST, inAmount).out(material, PLATE, outAmount).duration(realTime).energyIn(LV));
				});
			}
		}
		if (PLATE_DOUBLE.test(material)) {
			if (INGOT.test(material)) {
				calc(material, INGOT, PLATE_DOUBLE, (int) material.getMass(), LV, (inAmount, outAmount, time, energy) -> {
					event.create("double_%s_plate".formatted(material.getName()), NCRecipeTypes.BENDING_MACHINE, b -> b
						.in(material, INGOT, inAmount).out(material, PLATE_DOUBLE, outAmount).program(2).duration(time).energyIn(LV));
				});
			}
			if (PLATE.test(material)) {
				event.shaped("double_%s_plate".formatted(material.getName()), materials().getItem(material, PLATE_DOUBLE), b -> b
					.pattern("H", "a", "a").key('a', materials().getItemTag(material, PLATE)));
			}
			if (GEM_FLAWLESS.test(material)) {
				calc(material, GEM_FLAWLESS, PLATE_DOUBLE, (int) material.getMass(), LV, (inAmount, outAmount, time, energy) -> {
					event.create("double_%s_plate".formatted(material.getName()), NCRecipeTypes.LATHE, b -> b
						.in(material, GEM_FLAWLESS, inAmount).out(material, PLATE_DOUBLE, outAmount).duration(time).energyIn(LV));
				});
			}
			//Dust <-> plate will be handles by compressor machine later
		}
		if (PLATE_DENSE.test(material)) {
			if (INGOT.test(material)) {
				calc(material, INGOT, PLATE_DENSE, (int) material.getMass(), LV, (inAmount, outAmount, time, energy) -> {
					event.create("dense_%s_plate".formatted(material.getName()), NCRecipeTypes.BENDING_MACHINE, b -> b
						.in(material, INGOT, inAmount).out(material, PLATE_DENSE, outAmount).program(9).duration(time).energyIn(LV));
				});
			}
			if (GEM_EXQUISITE.test(material)) {
				calc(material, GEM_EXQUISITE, PLATE_DENSE, (int) material.getMass(), LV, (inAmount, outAmount, time, energy) -> {
					event.create("dense_%s_plate".formatted(material.getName()), NCRecipeTypes.LATHE, b -> b
						.in(material, GEM_EXQUISITE, inAmount).out(material, PLATE_DENSE, outAmount).duration(time).energyIn(LV));
				});
			}
			//Dust <-> plate will be handles by compressor machine later
		}
		if (GEAR.test(material)) {
			if (INGOT.test(material)) {
				calc(material, INGOT, GEAR, (int) material.getMass(), LV, (inAmount, outAmount, time, energy) -> {
					event.create("%s_gear".formatted(material.getName()), NCRecipeTypes.EXTRUDER, b -> b
						.in(material, INGOT, inAmount).nc(NCItems.EXTRUDER_SHAPES.get(ExtruderShape.GEAR)).out(material, GEAR, outAmount).duration(time).energyIn(LV));
				});
			}
			if (PLATE.test(material) && ROD.test(material)) {
				event.shaped("%s_gear".formatted(material.getName()), materials().getItem(material, GEAR), b -> b
					.pattern("aba", "bWb", "aba").key('a', ROD, material).key('b', PLATE, material));
			}
		}
		if (GEAR_SMALL.test(material)) {
			if (INGOT.test(material)) {
				calc(material, INGOT, GEAR_SMALL, (int) material.getMass(), LV, (inAmount, outAmount, time, energy) -> {
					event.create("small_%s_gear".formatted(material.getName()), NCRecipeTypes.EXTRUDER, b -> b
						.in(material, INGOT, inAmount).nc(NCItems.EXTRUDER_SHAPES.get(ExtruderShape.GEAR_SMALL)).out(material, GEAR_SMALL, outAmount).duration(time).energyIn(LV));
				});
			}
			if (PLATE.test(material) && ROD.test(material)) {
				event.shaped("small_%s_gear".formatted(material.getName()), materials().getItem(material, GEAR_SMALL), b -> b
					.pattern(" a ", "XbH", " a ").key('a', ROD, material).key('b', PLATE, material));
			}
		}
		if (ROD.test(material)) {
			if (INGOT.test(material)) {
				calc(material, INGOT, ROD, (int) material.getMass(), LV, (inAmount, outAmount, time, energy) -> {
					event.create("%s_rod".formatted(material.getName()), NCRecipeTypes.EXTRUDER, b -> b
						.in(material, INGOT, inAmount).nc(NCItems.EXTRUDER_SHAPES.get(ExtruderShape.ROD)).out(material, ROD, outAmount).duration(time).energyIn(LV));
				});
				calc(material, INGOT, ROD, (int) material.getMass(), LV, (inAmount, outAmount, time, energy) -> {
					event.create("%s_rod".formatted(material.getName()), NCRecipeTypes.LATHE, b -> b
						.in(material, INGOT, inAmount).out(material, ROD, outAmount).duration(time).energyIn(LV));
				});
			}
			if (GEM.test(material)) {
				calc(material, GEM, ROD, (int) material.getMass(), LV, (inAmount, outAmount, time, energy) -> {
					event.create("%s_rod".formatted(material.getName()), NCRecipeTypes.LATHE, b -> b
						.in(material, GEM, inAmount).out(material, ROD, outAmount).duration(time).energyIn(LV));
				});
			}
		}
		if (BOLT.test(material)) {
			if (INGOT.test(material)) {
				calc(material, INGOT, BOLT, (int) material.getMass(), LV, (inAmount, outAmount, time, energy) -> {
					event.create("%s_bolt".formatted(material.getName()), NCRecipeTypes.EXTRUDER, b -> b
						.in(material, INGOT, inAmount).nc(NCItems.EXTRUDER_SHAPES.get(ExtruderShape.BOLT)).out(material, BOLT, outAmount).duration(time).energyIn(LV));
				});
			}
			if (ROD.test(material)) {
				event.shapeless("%s_bolt".formatted(material.getName()), materials().getItem(material, BOLT, 2), b -> b
					.add(TAG_WIRE_CUTTERS).add(ROD, material));
			}
		}
		if (SCREW.test(material)) {
			if (INGOT.test(material)) {
				calc(material, INGOT, SCREW, (int) material.getMass(), LV, (inAmount, outAmount, time, energy) -> {
					event.create("%s_screw".formatted(material.getName()), NCRecipeTypes.EXTRUDER, b -> b
						.in(material, INGOT, inAmount).nc(NCItems.EXTRUDER_SHAPES.get(ExtruderShape.SCREW)).out(material, SCREW, outAmount).duration(time).energyIn(LV));
				});
			}
			if (BOLT.test(material)) {
				event.shapeless("%s_screw".formatted(material.getName()), materials().getItem(material, SCREW), b -> b
					.add(TAG_HAMMERS).add(BOLT, material, 2));
				calc(material, BOLT, SCREW, (int) material.getMass(), LV, (inAmount, outAmount, time, energy) -> {
					event.create("%s_screw".formatted(material.getName()), NCRecipeTypes.LATHE, b -> b
						.in(material, BOLT, inAmount).out(material, SCREW, outAmount).duration(time).energyIn(LV));
				});
			}
		}
		if (FOIL.test(material)) {
			if (INGOT.test(material)) {
				calc(material, INGOT, FOIL, (int) material.getMass(), LV, (inAmount, outAmount, time, energy) -> {
					event.create("%s_foil".formatted(material.getName()), NCRecipeTypes.BENDING_MACHINE, b -> b
						.in(material, INGOT, inAmount).out(material, FOIL, outAmount).program(10).duration(time).energyIn(LV));
				});
			}
			if (PLATE.test(material)) {
				event.shapeless("%s_foil".formatted(material.getName()), materials().getItem(material, FOIL), b -> b
					.add(TAG_HAMMERS).add(PLATE, material));
			}
		}
		if (RING.test(material)) {
			if (INGOT.test(material)) {
				calc(material, INGOT, RING, (int) material.getMass(), LV, (inAmount, outAmount, time, energy) -> {
					event.create("%s_ring".formatted(material.getName()), NCRecipeTypes.EXTRUDER, b -> b
						.in(material, INGOT, inAmount).nc(NCItems.EXTRUDER_SHAPES.get(ExtruderShape.RING)).out(material, RING, outAmount).duration(time).energyIn(LV));
				});
			}
			if (ROD.test(material)) {
				event.shapeless("%s_ring".formatted(material.getName()), materials().getItem(material, RING), b -> b
					.add(TAG_HAMMERS).add(ROD, material));
				calc(material, ROD, RING, (int) material.getMass(), LV, (inAmount, outAmount, time, energy) -> {
					event.create("%s_ring".formatted(material.getName()), NCRecipeTypes.BENDING_MACHINE, b -> b
						.in(material, ROD, inAmount).out(material, RING, outAmount).duration(time).energyIn(LV));
				});
			}
		}
		if (ROTOR.test(material)) {
			if (INGOT.test(material)) {
				calc(material, INGOT, ROTOR, (int) material.getMass(), LV, (inAmount, outAmount, time, energy) -> {
					event.create("%s_rotor".formatted(material.getName()), NCRecipeTypes.EXTRUDER, b -> b
						.in(material, INGOT, inAmount).nc(NCItems.EXTRUDER_SHAPES.get(ExtruderShape.ROTOR)).out(material, ROTOR, outAmount).duration(time).energyIn(LV));
				});
			}
			if (PLATE.test(material) && RING.test(material) && SCREW.test(material)) {
				event.shaped("%s_rotor".formatted(material.getName()), materials().getItem(material, ROTOR), b -> b
					.pattern("aWa", "bcH", "aXa").key('a', PLATE, material).key('b', SCREW, material).key('c', RING, material));
			}
		}
		if (FINE_WIRE.test(material)) {
			if (INGOT.test(material)) {
				calc(material, INGOT, FINE_WIRE, (int) material.getMass(), LV, (inAmount, outAmount, time, energy) -> {
					event.create("fine_%s_wire".formatted(material.getName()), NCRecipeTypes.WIREMILL, b -> b
						.in(material, INGOT, inAmount).out(material, FINE_WIRE, outAmount).program(3).duration(time).energyIn(LV));
				});
			}
			if (FOIL.test(material)) {
				event.shapeless("fine_%s_wire".formatted(material.getName()), materials().getItem(material, FINE_WIRE), b -> b
					.add(TAG_WIRE_CUTTERS).add(FOIL, material));
			}
			if (DUST.test(material)) {
				if (!INGOT.test(material)) {
					calc(material, DUST, FINE_WIRE, (int) material.getMass(), LV, (inAmount, outAmount, time, energy) -> {
						event.create("fine_%s_wire".formatted(material.getName()), NCRecipeTypes.WIREMILL, b -> b
							.in(material, DUST, inAmount).out(material, FINE_WIRE, outAmount).program(3).duration(time).energyIn(LV));
					});
				}
			}
		}
		if (FRAME_BOX.test(material)) {
			if (ROD.test(material)) {
				event.shaped("%s_frame_box".formatted(material.getName()), materials().getItem(material, FRAME_BOX, 2), b -> b
					.pattern("aaa", "aWa", "aaa").key('a', ROD, material));
				calc(material, ROD, FRAME_BOX, (int) material.getMass(), LV, (inAmount, outAmount, time, energy) -> {
					event.create("%s_frame_box".formatted(material.getName()), NCRecipeTypes.ASSEMBLING_MACHINE, b -> b
						.in(material, ROD, inAmount).out(material, FRAME_BOX, outAmount).program(4).duration(time).energyIn(LV));
				});
			}
		}
	}

	private static void addWireRecipes(final RegisterRecipeEvent event, final Material material) {
		for (final WireType wireType : WireType.values()) {
			if (!wireType.getHandler().test(material)) {
				continue;
			}
			if (INGOT.test(material)) {
				calc(material, INGOT, wireType.getHandler(), (int) material.getMass(), LV, (inAmount, outAmount, time, energy) -> {
					event.create(wireType.getHandler().getUnlocalizedName(material), NCRecipeTypes.WIREMILL, b -> {
						b.in(material, INGOT, inAmount).out(material, wireType.getHandler(), outAmount).program(wireType.getAmperage()).energyIn(LV);
						if (wireType.ordinal() == 0) {
							b.duration(time / 4 - 10);
						} else {
							b.duration(time / 2 - (wireType.ordinal() * 10));
						}
					});
				});
			}
			if (wireType != WireType.WIRE_1X && WIRE_1X.test(material)) {
				event.shapeless("1x_%s_wire_from_%s_wire".formatted(material.getName(), wireType.getAmperage()), WIRE_1X, material, wireType.getAmperage(), b -> b.add(wireType.getHandler(), material));
			}
		}
		if (WIRE_1X.test(material)) {
			if (WIRE_2X.test(material)) {
				event.shapeless("2x_%s_wire_from_1x_wire".formatted(material.getName()), WIRE_2X, material, b -> b.add(WIRE_1X, material, 2));
			}
			if (WIRE_4X.test(material)) {
				event.shapeless("4x_%s_wire_from_1x_wire".formatted(material.getName()), WIRE_4X, material, b -> b.add(WIRE_1X, material, 4));
			}
			if (WIRE_8X.test(material)) {
				event.shapeless("8x_%s_wire_from_1x_wire".formatted(material.getName()), WIRE_8X, material, b -> b.add(WIRE_1X, material, 8));
			}
		}
		if (WIRE_2X.test(material)) {
			if (WIRE_4X.test(material)) {
				event.shapeless("4x_%s_wire_from_2x_wire".formatted(material.getName()), WIRE_4X, material, b -> b.add(WIRE_2X, material, 2));
			}
			if (WIRE_8X.test(material)) {
				event.shapeless("8x_%s_wire_from_2x_wire".formatted(material.getName()), WIRE_8X, material, b -> b.add(WIRE_2X, material, 4));
			}
			if (WIRE_12X.test(material)) {
				event.shapeless("12x_%s_wire_from_2x_wire".formatted(material.getName()), WIRE_12X, material, b -> b.add(WIRE_2X, material, 6));
			}
			if (WIRE_16X.test(material)) {
				event.shapeless("16x_%s_wire_from_2x_wire".formatted(material.getName()), WIRE_16X, material, b -> b.add(WIRE_2X, material, 8));
			}
		}
		if (WIRE_4X.test(material)) {
			if (WIRE_8X.test(material)) {
				event.shapeless("8x_%s_wire_from_4x_wire".formatted(material.getName()), WIRE_8X, material, b -> b.add(WIRE_4X, material, 2));
			}
			if (WIRE_12X.test(material)) {
				event.shapeless("12x_%s_wire_from_4x_wire".formatted(material.getName()), WIRE_12X, material, b -> b.add(WIRE_4X, material, 3));
			}
			if (WIRE_16X.test(material)) {
				event.shapeless("16x_%s_wire_from_4x_wire".formatted(material.getName()), WIRE_16X, material, b -> b.add(WIRE_4X, material, 4));
			}
		}
		if (WIRE_8X.test(material)) {
			if (WIRE_16X.test(material)) {
				event.shapeless("16x_%s_wire_from_8x_wire".formatted(material.getName()), WIRE_16X, material, b -> b.add(WIRE_8X, material, 2));
			}
		}
		if (WIRE_4X.test(material) && WIRE_8X.test(material) && WIRE_12X.test(material)) {
			event.shapeless("12x_%s_wire_from_4x_wire_and_8x_wire".formatted(material.getName()), WIRE_12X, material, b -> b.add(WIRE_4X, material).add(WIRE_8X, material));

		}
		if (WIRE_4X.test(material) && WIRE_12X.test(material) && WIRE_16X.test(material)) {
			event.shapeless("16x_%s_wire_from_4x_wire_and_12x_wire".formatted(material.getName()), WIRE_16X, material, b -> b.add(WIRE_4X, material).add(WIRE_12X, material));
		}
	}

	private static void addBlastRecipes(final RegisterRecipeEvent event, final Material material, final MaterialTraitBlast trait) {
		if (DUST.test(material) && INGOT.test(material)) {
			calc(material, DUST, INGOT, (int) material.getMass(), trait.getRecipeTier(), (inAmount, outAmount, time, energy) -> {
				event.create("%s_ingot".formatted(material.getName()), NCRecipeTypes.ELECTRIC_BLAST_FURNACE, b -> b
					.in(material, DUST).out(material, INGOT).duration(time).energyIn(trait.getRecipeTier()).data(RecipeDataTokens.BLAST_TEMP, trait.getTemperature()));
			});
		}
	}

	private MaterialRecipes() {
	}
}
