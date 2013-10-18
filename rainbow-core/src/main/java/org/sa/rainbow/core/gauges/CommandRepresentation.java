package org.sa.rainbow.core.gauges;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sa.rainbow.core.models.commands.IRainbowModelCommandRepresentation;
import org.sa.rainbow.core.util.TypedAttribute;
import org.sa.rainbow.util.HashCodeUtil;

public class CommandRepresentation implements IRainbowModelCommandRepresentation, Cloneable {

    private String[] m_parameters;
    private String   m_label;
    private String   m_target;
    private String   m_commandName;
    private String   m_modelName;
    private String   m_modelType;

    public CommandRepresentation (IRainbowModelCommandRepresentation cmd) {
        m_label = cmd.getLabel ();
        m_parameters = new String[cmd.getParameters ().length];
        String[] parameters = cmd.getParameters ();
        for (int i = 0; i < parameters.length; i++) {
            m_parameters[i] = parameters[i];
        }
        m_target = cmd.getTarget ();
        m_commandName = cmd.getCommandName ();
        m_modelName = cmd.getModelName ();
        m_modelType = cmd.getModelType ();

    }

    public CommandRepresentation (String label, String commandName, String modelName, String modelType, String target,
            String... parameters) {
        m_label = label;
        m_commandName = commandName;
        m_modelName = modelName;
        m_modelType = modelType;
        m_target = target;
        m_parameters = parameters;

    }

    @Override
    public String getLabel () {
        return m_label;
    }

    @Override
    public String[] getParameters () {
        return m_parameters;
    }

    @Override
    public String getTarget () {
        return m_target;
    }

    @Override
    public String getCommandName () {
        return m_commandName;
    }

    @Override
    public String getModelName () {
        return m_modelName;
    }

    @Override
    public String getModelType () {
        return m_modelType;
    }

    @Override
    protected CommandRepresentation clone () throws CloneNotSupportedException {
        return (CommandRepresentation )super.clone ();
    }

    @Override
    public boolean equals (Object obj) {
        if (obj != this) {
            if (obj instanceof CommandRepresentation) {
                CommandRepresentation cr = (CommandRepresentation )obj;
                return (cr.getModelType () == getModelType () || (getModelType () != null && getModelType ().equals (
                        cr.getModelType ())))
                        && (cr.getCommandName () == getCommandName () || (getCommandName () != null && getCommandName ()
                        .equals (cr.getCommandName ())))
                        && (cr.getLabel () == getLabel () || (getLabel () != null && getLabel ()
                        .equals (cr.getLabel ())))
                        && (cr.getTarget () == getTarget () || (getTarget () != null && getTarget ().equals (
                                cr.getTarget ()))) && Arrays.equals (getParameters (), cr.getParameters ());
            }
            return false;
        }
        return true;
    }

    @Override
    public int hashCode () {
        int result = HashCodeUtil.SEED;
        result = HashCodeUtil.hash (result, getModelType ());
        result = HashCodeUtil.hash (result, getModelName ());
        result = HashCodeUtil.hash (result, getCommandName ());
        result = HashCodeUtil.hash (result, getLabel ());
        result = HashCodeUtil.hash (result, getTarget ());
        result = HashCodeUtil.hash (result, getParameters ());
        return result;
    }

    void setModel (String name, String type) {
        m_modelName = name;
        m_modelType = type;
    }

    void setModel (TypedAttribute modelRef) {
        m_modelName = modelRef.getName ();
        m_modelType = modelRef.getType ();
    }

    static Pattern pattern = Pattern.compile ("(([\\w\\$\\<\\>\\\"]+)\\.)?(\\w+)\\s*\\(([\\w, \\$\\<\\>\\\"]*)\\)");

    public static CommandRepresentation parseCommandSignature (String commandSignature) {
        Matcher matcher = pattern.matcher (commandSignature);
        if (matcher.find ()) {
            String target = matcher.group (2);
            String commandName = matcher.group (3);
            String unprocessedParams = matcher.group (4);
            String[] parameters = new String[0];
            if (unprocessedParams != null) {
                parameters = unprocessedParams.split ("[\\s,]");
            }
            CommandRepresentation rep = new CommandRepresentation (commandName, commandName, null, null, target,
                    parameters);
            return rep;
        }
        return null;
    }

}
