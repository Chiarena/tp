package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.student.Address;
import seedu.address.model.student.Email;
import seedu.address.model.student.FeeStatus;
import seedu.address.model.student.Lesson;
import seedu.address.model.student.Name;
import seedu.address.model.student.Phone;
import seedu.address.model.student.Remark;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

/**
 * Jackson-friendly version of {@link Student}.
 */
class JsonAdaptedStudent {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Student's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String feeStatus;
    private final String address;
    private final List<JsonAdaptedLesson> lessons = new ArrayList<>();
    private final String remark;
    private final String subject;


    /**
     * Constructs a {@code JsonAdaptedStudent} with the given student details.
     */
    @JsonCreator
    public JsonAdaptedStudent(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
                              @JsonProperty("email") String email, @JsonProperty("address") String address,
                              @JsonProperty("subject") String subject, @JsonProperty("remark") String remark,
                              @JsonProperty("feeStatus") String feeStatus,
                              @JsonProperty("lessons") List<JsonAdaptedLesson> lessons) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.feeStatus = feeStatus;
        this.address = address;
        this.subject = subject;
        if (lessons != null) {
            this.lessons.addAll(lessons);
        }
        this.remark = remark;
    }

    /**
     * Converts a given {@code Student} into this class for Jackson use.
     */
    public JsonAdaptedStudent(Student source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        feeStatus = source.getFeeStatus().status;
        address = source.getAddress().value;
        subject = source.getSubject().value;
        remark = source.getRemark().value;
        lessons.addAll(source.getLessons().stream()
                .map(lesson -> new JsonAdaptedLesson(lesson.getJsonValue()))
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted student object into the model's {@code Student} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted student.
     */
    public Student toModelType() throws IllegalValueException {
        final List<Lesson> modelLessons = new ArrayList<>();
        for (JsonAdaptedLesson lesson : lessons) {
            modelLessons.add(lesson.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        if (subject == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Subject.class.getSimpleName()));
        }
        if (feeStatus == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    FeeStatus.class.getSimpleName()));
        }
        final FeeStatus modelFeeStatus = new FeeStatus(feeStatus);
        final Remark modelRemark = new Remark(remark);
        final Address modelAddress = new Address(address);
        final Subject modelSubject = new Subject(subject);

        return new Student(modelName, modelPhone, modelEmail, modelAddress, modelSubject,
                modelRemark, modelFeeStatus, modelLessons);
    }

}
