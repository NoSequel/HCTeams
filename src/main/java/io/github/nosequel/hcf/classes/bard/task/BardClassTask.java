package io.github.nosequel.hcf.classes.bard.task;

import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.classes.ClassController;
import io.github.nosequel.hcf.classes.bard.BardClass;
import io.github.nosequel.hcf.classes.bard.BardClassData;
import io.github.nosequel.hcf.tasks.Task;

public class BardClassTask extends Task {

    private final ClassController classController = HCTeams.getInstance().getHandler().findController(ClassController.class);
    private final BardClass bardClass = classController.findClass(BardClass.class);

    public BardClassTask() {
        super(20);
    }

    @Override
    public void tick() throws Exception {
        bardClass.getEquipped().forEach(player -> {
            if (bardClass.getClassData().containsKey(player)) {
                final BardClassData classData = bardClass.getClassData().get(player);
                final long newEnergy = classData.getEnergy() + 1;

                classData.setEnergy(newEnergy);
            }

        });
    }

    @Override
    public String getName() {
        return "BardClass-Task";
    }
}