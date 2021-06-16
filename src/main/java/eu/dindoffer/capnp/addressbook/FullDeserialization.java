package eu.dindoffer.capnp.addressbook;

import org.capnproto.DecodeException;
import org.capnproto.MessageReader;
import org.capnproto.Serialize;
import org.capnproto.Text;
import org.capnproto.examples.Addressbook.AddressBook;
import org.capnproto.examples.Addressbook.Person;

import java.io.ByteArrayInputStream;
import java.nio.BufferUnderflowException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class FullDeserialization {

    public static void fuzzerTestOneInput(byte[] input) {
        MessageReader message;
        try {
            ReadableByteChannel channel = Channels.newChannel(new ByteArrayInputStream(input));
            message = Serialize.read(channel);
        } catch (Exception e) {
            return;
        }

        try {
            AddressBook.Reader addressbook = message.getRoot(AddressBook.factory);

            if (addressbook.hasPeople()) {
                for (Person.Reader person : addressbook.getPeople()) {
                    if (person.hasName()) {
                        Text.Reader name = person.getName();
                        String nameString = name.toString();
                    }
                    if (person.hasEmail()) {
                        Text.Reader email = person.getEmail();
                        String emailString = email.toString();
                    }

                    if (person.hasPhones()) {
                        for (Person.PhoneNumber.Reader phone : person.getPhones()) {
                            String typeName = "UNKNOWN";
                            switch (phone.getType()) {
                                case MOBILE:
                                    typeName = "mobile";
                                    break;
                                case HOME:
                                    typeName = "home";
                                    break;
                                case WORK:
                                    typeName = "work";
                                    break;
                            }
                            if (phone.hasNumber()) {
                                Text.Reader number = phone.getNumber();
                                String numberString = number.toString();
                            }
                        }

                        Person.Employment.Reader employment = person.getEmployment();
                        switch (employment.which()) {
                            case UNEMPLOYED:
                                break;
                            case EMPLOYER:
                                Text.Reader employer = employment.getEmployer();
                                String employerString = employer.toString();
                                break;
                            case SCHOOL:
                                Text.Reader school = employment.getSchool();
                                String getSchool = school.toString();
                                break;
                            case SELF_EMPLOYED:
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        } catch (DecodeException | IndexOutOfBoundsException | BufferUnderflowException e) {
            return;
        }
    }
}
