package mk.ukim.finki.wp.fcseservices.model.nns;

public enum TopicStatus
{
    REQUESTED, SCHEDULED, UNDER_DISCUSSION, FINISHED, CANCELED;

    public String statusName()
    {
        switch(this.name())
        {
            case "REQUESTED":
                return "Предлог";
            case "SCHEDULED":
                return "Закажан";
            case "UNDER_DISCUSSION":
                return "Моментална точка";
            case "FINISHED":
                return "Завршен";
            case "CANCELED":
                return "Откажан";
        }
        return this.name();
    }

    @Override
    public String toString() {
        return name();
    }
}
