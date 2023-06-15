using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace HVK.Models
{
    public class CustomerValidation
    {
        public class CompareToday : ValidationAttribute
        {
            protected override ValidationResult IsValid(object value, ValidationContext validationContext)
            {
                DateTime dt = (DateTime)value;
                if (dt >= DateTime.Today)
                {
                    return ValidationResult.Success;
                }

                return new ValidationResult("Make sure your date is greater or equal to today");
            }

        }//CompareToday


        public sealed class CompareDates : ValidationAttribute
        {
            private string _dateToCompare;

            public CompareDates(string dateToCompare)
            {
                _dateToCompare = dateToCompare;
            }
            protected override ValidationResult IsValid(object value, ValidationContext validationContext)
            {
                var dateToCompare = validationContext.ObjectType.GetProperty(_dateToCompare);
                var dateToCompareValue = dateToCompare.GetValue(validationContext.ObjectInstance, null);
                if (dateToCompareValue != null && value != null && (DateTime)value < (DateTime)dateToCompareValue)
                {
                    return new ValidationResult("End date must be greater or equal to Start date");
                }
                return null;
            }
        }//CompareDates


    }
}
