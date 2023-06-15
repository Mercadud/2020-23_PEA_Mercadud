using System;
using System.Collections.Generic;

#nullable disable

namespace HVK.Models
{
    public partial class Discount
    {
        public Discount()
        {
            PetReservationDiscounts = new HashSet<PetReservationDiscount>();
            ReservationDiscounts = new HashSet<ReservationDiscount>();
        }

        public int DiscountId { get; set; }
        public string Desciption { get; set; }
        public decimal Percentage { get; set; }
        public string Type { get; set; }

        public virtual ICollection<PetReservationDiscount> PetReservationDiscounts { get; set; }
        public virtual ICollection<ReservationDiscount> ReservationDiscounts { get; set; }
    }
}
