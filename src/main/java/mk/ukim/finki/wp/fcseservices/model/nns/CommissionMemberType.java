package mk.ukim.finki.wp.fcseservices.model.nns;

public enum CommissionMemberType
{
    PRESIDENT, MEMBER, MENTOR;

    public String typeName()
    {
        switch(this.name())
        {
            case "PRESIDENT":
                return "Претседател";
            case "MEMBER":
                return "Член";
            case "MENTOR":
                return "Ментор";
        }

        return this.name();
    }
}
