using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace HVK.Models {
    public class Login : Customer {

        [Required]
        [DataType(DataType.EmailAddress)]
        public new string Email { get; set; }

        public sealed class PasswordValidation : ValidationAttribute {

            public string Email { get; private set; }

            public PasswordValidation(string e) {
                Email = e;
            }

            protected override ValidationResult IsValid(object value, ValidationContext validationContext) {

                var em = validationContext.ObjectType.GetProperty(Email).GetValue(validationContext.ObjectInstance, null);

                HVK_MSSQL_W22Team4Context e = (HVK_MSSQL_W22Team4Context)validationContext.GetService(typeof(HVK_MSSQL_W22Team4Context));
                var custFound = e.Customers.Where(t => t.Email == (string)em).FirstOrDefault();
                Console.WriteLine(custFound);
                if (custFound != null) {
                    if (custFound.Password == (string)value) {
                        return ValidationResult.Success;
                    }
                }
                return new ValidationResult("Invalid Email/Password");
            }
        }

        [Required]
        [DataType(DataType.Password)]
        [PasswordValidation("Email")]
        public new string Password { get; set; }

    }
}
