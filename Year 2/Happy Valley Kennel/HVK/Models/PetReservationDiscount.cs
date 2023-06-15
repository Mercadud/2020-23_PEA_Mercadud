using System;
using System.Collections.Generic;

#nullable disable

namespace HVK.Models
{
    public partial class PetReservationDiscount
    {
        public int DiscountId { get; set; }
        public int PetReservationId { get; set; }

        public virtual Discount Discount { get; set; }
        public virtual PetReservation PetReservation { get; set; }
    }
}
