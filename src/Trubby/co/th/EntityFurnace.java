package Trubby.co.th;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.server.v1_7_R4.Block;
import net.minecraft.server.v1_7_R4.Blocks;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.TileEntityFurnace;

import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.v1_7_R4.block.CraftFurnace;
import org.bukkit.inventory.InventoryHolder;

public class EntityFurnace extends TileEntityFurnace {

	public EntityFurnace(EntityHuman entity) {
		this.world = entity.world;
	}

	@Override
	public boolean a(EntityHuman entityhuman) {
		return true;
	}

	@Override
	public int p() {
		return 0;
	}

	@Override
	public void update() {

	}

	@Override
	public Block q() {
		return Blocks.FURNACE;
	}

	@Override
	public InventoryHolder getOwner() {
		Furnace furnace = new CraftFurnace(this.world.getWorld().getBlockAt(0, 0, 0));

		/**
		 * Setting the tile we will use, this is the only good way!
		 */
		try {
			Field field = CraftFurnace.class.getDeclaredField("furnace");
			field.setAccessible(true);

			Field mfield = Field.class.getDeclaredField("modifiers");
			mfield.setAccessible(true);
			mfield.set(field, field.getModifiers() & ~Modifier.FINAL);

			field.set(furnace, this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return furnace;
	}
}