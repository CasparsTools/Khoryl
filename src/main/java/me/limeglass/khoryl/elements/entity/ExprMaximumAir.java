package me.limeglass.khoryl.elements.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.util.Timespan;
import ch.njol.skript.util.Version;
import ch.njol.util.coll.CollectionUtils;

@Name("Maximum Air")
@Description("Returns the maximum amount of air the living entity can have, in ticks.")
@Examples("set {_max} to the maximum air of target entity")
@Since("1.0.0")
public class ExprMaximumAir extends SimplePropertyExpression<LivingEntity, Timespan> {

	static {
		if (!Skript.getMinecraftVersion().isSmallerThan(new Version(1, 10)))
			register(ExprMaximumAir.class, Timespan.class, "max[imum] air", "livingentities");
	}

	@Override
	public Class<? extends Timespan> getReturnType() {
		return Timespan.class;
	}

	@Override
	protected String getPropertyName() {
		return "maximum air";
	}

	@Override
	@Nullable
	public Timespan convert(LivingEntity entity) {
		return Timespan.fromTicks_i(entity.getMaximumAir());
	}

	@Nullable
	@Override
	public Class<?>[] acceptChange(ChangeMode mode) {
		if (mode == ChangeMode.ADD || mode == ChangeMode.REMOVE || mode == ChangeMode.SET)
			return CollectionUtils.array(Timespan.class);
		return null;
	}

	@Override
	public void change(Event event, @Nullable Object[] delta, ChangeMode mode) {
		if (delta[0] == null)
			return;
		int ticks = (int) ((Timespan) delta[0]).getTicks_i();
		switch (mode) {
			case ADD:
				for (LivingEntity entity : getExpr().getArray(event)) {
					int existing = entity.getMaximumAir();
					entity.setMaximumAir(existing + ticks);
				}
				break;
			case REMOVE:
				for (LivingEntity entity : getExpr().getArray(event)) {
					int existing = entity.getMaximumAir();
					entity.setMaximumAir(existing - ticks);
				}
				break;
			case SET:
				for (LivingEntity entity : getExpr().getArray(event))
					entity.setMaximumAir(ticks);
				break;
			case REMOVE_ALL:
			case DELETE:
			case RESET:
			default:
				break;
		}
	}

}
