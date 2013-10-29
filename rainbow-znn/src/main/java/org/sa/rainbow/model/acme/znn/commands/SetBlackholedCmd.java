package org.sa.rainbow.model.acme.znn.commands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.acmestudio.acme.PropertyHelper;
import org.acmestudio.acme.element.IAcmeComponent;
import org.acmestudio.acme.element.property.IAcmeProperty;
import org.acmestudio.acme.element.property.IAcmePropertyValue;
import org.acmestudio.acme.model.command.IAcmeCommand;
import org.acmestudio.acme.model.command.IAcmePropertyCommand;
import org.sa.rainbow.core.error.RainbowModelException;
import org.sa.rainbow.model.acme.AcmeModelInstance;

public class SetBlackholedCmd extends ZNNAcmeModelCommand<IAcmeProperty> {

    public SetBlackholedCmd (String commandName, AcmeModelInstance model, String target, String ipSet) {
        super (commandName, model, target, ipSet);
    }

    @Override
    public IAcmeProperty getResult () throws IllegalStateException {
        return ((IAcmePropertyCommand )m_command).getProperty ();
    }


    @Override
    protected List<IAcmeCommand<?>> doConstructCommand () throws RainbowModelException {
        IAcmeComponent server = getModelContext ().resolveInModel (getTarget (), IAcmeComponent.class);
        String[] split = getParameters ()[0].split (",");
        HashSet<String> set = new HashSet<> ();
        set.addAll (Arrays.asList (split));
        IAcmeProperty property = server.getProperty ("blackholed");
        IAcmePropertyValue acmeVal = PropertyHelper.toAcmeVal (set);
        List<IAcmeCommand<?>> cmds = new LinkedList<> ();
        if (propertyValueChanging (property, acmeVal)) {
            m_command = server.getCommandFactory ().propertyValueSetCommand (property, acmeVal);
            cmds.add (m_command);
        }
        return cmds;
    }

}