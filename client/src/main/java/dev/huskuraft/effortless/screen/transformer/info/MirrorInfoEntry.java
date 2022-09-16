package dev.huskuraft.effortless.screen.transformer.info;

import dev.huskuraft.effortless.building.pattern.mirror.Mirror;
import dev.huskuraft.effortless.core.Axis;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.EntryList;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.text.TextStyle;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MirrorInfoEntry extends TransformerInfoEntry<Mirror> {

    public MirrorInfoEntry(Entrance entrance, EntryList entryList, Mirror mirror) {
        super(entrance, entryList, mirror);
    }

    @Override
    protected List<Text> getBasicComponents(Mirror transformer) {
        var position = Text.text("Position: %s", getPositionDescription(transformer.position())).withStyle(TextStyle.GRAY);
        var axis = Text.text("Plane: %s", getPlaneDescription(transformer.axis())).withStyle(TextStyle.GRAY);
        return Stream.of(position, axis).collect(Collectors.toList());
    }

    @Override
    protected Text getDisplayName(Mirror transformer) {
        return Text.text("Mirror Transformer");
    }

    private Text getPlaneDescription(Axis axis) {
        if (axis == null) return Text.translate("effortless.axis.undefined");
        return switch (axis) {
            case X -> Text.translate("effortless.axis.x");
            case Y -> Text.translate("effortless.axis.y");
            case Z -> Text.translate("effortless.axis.z");
        };
    }

}
