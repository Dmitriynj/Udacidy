package by.epam.dmitriytomashevich.javatr.courses.domain;

import java.util.Objects;

public class RequestForm {
    private Long id;
    private Long sectionId;
    private Long requestId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestForm that = (RequestForm) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(sectionId, that.sectionId) &&
                Objects.equals(requestId, that.requestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sectionId, requestId);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }
}
